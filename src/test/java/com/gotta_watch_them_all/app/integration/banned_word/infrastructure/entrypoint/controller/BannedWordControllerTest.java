package com.gotta_watch_them_all.app.integration.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.helper.AuthHelper;
import com.gotta_watch_them_all.app.helper.AuthHelperData;
import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Set;

import static com.gotta_watch_them_all.app.helper.JsonHelper.objectToJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BannedWordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private RoleDao roleDao;

    private AuthHelperData adminHelperData;

    @MockBean
    private SaveOneBannedWord mockSaveOneBannedWord;

    @BeforeAll
    void initAll() {
        var adminRole = roleDao.findByRoleName(RoleName.ROLE_ADMIN);
        adminHelperData = authHelper.createUserAndGetAuthData("adminName", "admin@name.fr", "adminPassword", Set.of(adminRole));
    }

    @DisplayName("POST /api/banned-word")
    @Nested
    class SaveOneBannedWordTest {
        @Test
        void when_user_not_authenticate_should_send_unauthorized_error_response() throws Exception {
            mockMvc.perform(
                    post("/api/banned-word")
            )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_user_is_not_admin_should_send_forbidden_error_response() throws Exception {
            var userRole = roleDao.findByRoleName(RoleName.ROLE_USER);
            var userAuthHelper = authHelper
                    .createUserAndGetAuthData("username", "user@name.fr", "password", Set.of(userRole));
            mockMvc.perform(
                    post("/api/banned-word")
                            .header("Authorization", "Bearer " + userAuthHelper.getJwtToken())
            )
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\n", "\t"})
        void when_request_word_property_not_correct_should_send_bad_request_error_response(String incorrectWord) throws Exception {
            var saveBannedWordRequest = new SaveBannedWordRequest()
                    .setWord(incorrectWord);

            mockMvc.perform(
                    post("/api/banned-word")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectToJson(saveBannedWordRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void when_request_correct_should_call_usecase_save_one_banned_word() throws Exception {
            var saveBannedWordRequest = new SaveBannedWordRequest()
                    .setWord("F word");

            mockMvc.perform(
                    post("/api/banned-word")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectToJson(saveBannedWordRequest)));

            verify(mockSaveOneBannedWord, times(1)).execute("F word");
        }

        @Test
        void when_usecase_throw_already_created_exception_should_send_forbidden_error_response() throws Exception {
            var saveBannedWordRequest = new SaveBannedWordRequest()
                    .setWord("F word");
            when(mockSaveOneBannedWord.execute("F word")).thenThrow(new AlreadyCreatedException("already created"));

            mockMvc.perform(
                    post("/api/banned-word")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectToJson(saveBannedWordRequest)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void when_usecase_return_id_should_return_created_success_response_with_uri() throws Exception {
            var saveBannedWordRequest = new SaveBannedWordRequest()
                    .setWord("F word");
            when(mockSaveOneBannedWord.execute("F word")).thenReturn(31L);

            var location = mockMvc.perform(
                    post("/api/banned-word")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectToJson(saveBannedWordRequest)))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getHeader("Location");

            var response = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/api/banned-word/{id}")
                    .buildAndExpand(31L)
                    .toUri();
            assertThat(location).isEqualTo(response.toString());
        }
    }
}