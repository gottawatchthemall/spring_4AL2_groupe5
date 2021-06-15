package com.gotta_watch_them_all.app.integration.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
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

import java.util.Set;

import static com.gotta_watch_them_all.app.helper.JsonHelper.objectToJson;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
                    .andExpect(status().isForbidden());
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
    }
}