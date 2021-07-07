package com.gotta_watch_them_all.app.integration.user.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.helper.AuthHelper;
import com.gotta_watch_them_all.app.helper.AuthHelperData;
import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.user.usecase.FindAllUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
            ).andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\n", "\t", "notBoolean", "1.2"})
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
            verify(mockFindAllUser, times(1)).execute(true);
        }
    }
}