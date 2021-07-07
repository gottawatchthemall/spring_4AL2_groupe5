package com.gotta_watch_them_all.app.integration.user.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.helper.AuthHelper;
import com.gotta_watch_them_all.app.helper.AuthHelperData;
import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.user.core.dto.DtoUser;
import com.gotta_watch_them_all.app.user.usecase.FindAllUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.gotta_watch_them_all.app.helper.JsonHelper.jsonToObject;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private RoleDao roleDao;

    private AuthHelperData adminHelperData;

    private AuthHelperData userHelperData;

    @MockBean
    private FindAllUser mockFindAllUser;

    @BeforeAll
    void initAll() {
        var adminRole = roleDao.findByRoleName(RoleName.ROLE_ADMIN);
        adminHelperData = authHelper.createUserAndGetAuthData("adminName", "admin@name.fr", "adminPassword", Set.of(adminRole));
        var userRole = roleDao.findByRoleName(RoleName.ROLE_USER);
        userHelperData = authHelper
                .createUserAndGetAuthData("username", "user@name.fr", "password", Set.of(userRole));
    }

    @DisplayName("GET /api/user")
    @Nested
    class FindAllTest {
        @Test
        void when_user_not_authenticate_should_send_unauthorized_error_response() throws Exception {
            mockMvc.perform(
                    get("/api/user")
            ).andExpect(status().isUnauthorized());
        }

        @Test
        void when_user_not_admin_should_send_forbidden_error_response() throws Exception {
            mockMvc.perform(
                    get("/api/user")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            ).andExpect(status().isForbidden());
        }

        @Test
        void when_get_aall_when_usecase_findAllUser_return_set_dto_user_should_send_success_with_set_dto_users() throws Exception {
            var setDtoUser = Set.of(new DtoUser().setId(3L).setName("user").setEmail("user@gmail.com").setVulgar(true));
            when(mockFindAllUser.execute(Optional.empty())).thenReturn(setDtoUser);
            var contentAsString = mockMvc.perform(
                    get("/api/user")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            var dtoUsers = jsonToObject(contentAsString, DtoUser[].class);
            assertThat(dtoUsers).isNotNull();
            assertThat(dtoUsers.length).isEqualTo(1);
            var result = new HashSet<>(Arrays.asList(dtoUsers));
            assertThat(result).isEqualTo(setDtoUser);
        }

        @ParameterizedTest
        @ValueSource(strings = {"notBoolean", "1.2"})
        void when_request_param_vulgar_is_null_should_return_bad_request(String emptySource) throws Exception {
            mockMvc.perform(
                    get("/api/user?vulgar=" + emptySource)
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            ).andExpect(status().isBadRequest());
        }

        @Test
        void when_request_param_vulgar_is_true_should_call_usecase_findAllUser() throws Exception {
            mockMvc.perform(
                    get("/api/user?vulgar=true")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            );
            verify(mockFindAllUser, times(1)).execute(Optional.of(true));
        }

        @Test
        void when_get_all_with_vulgar_request_param_to_true_and_usecase_findAllUser_return_set_dto_user_should_send_success_with_set_dto_users() throws Exception {
            var setDtoUser = Set.of(new DtoUser().setId(3L).setName("user").setEmail("user@gmail.com").setVulgar(true));
            when(mockFindAllUser.execute(Optional.of(true))).thenReturn(setDtoUser);
            var contentAsString = mockMvc.perform(
                    get("/api/user?vulgar=true")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            var dtoUsers = jsonToObject(contentAsString, DtoUser[].class);
            assertThat(dtoUsers).isNotNull();
            assertThat(dtoUsers.length).isEqualTo(1);
            var result = new HashSet<>(Arrays.asList(dtoUsers));
            assertThat(result).isEqualTo(setDtoUser);
        }
    }
}