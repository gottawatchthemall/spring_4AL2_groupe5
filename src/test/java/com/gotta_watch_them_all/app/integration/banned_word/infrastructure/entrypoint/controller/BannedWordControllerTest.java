package com.gotta_watch_them_all.app.integration.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.banned_word.usecase.DeleteBannedWordById;
import com.gotta_watch_them_all.app.banned_word.usecase.FindAllBannedWords;
import com.gotta_watch_them_all.app.banned_word.usecase.FindOneBannedWordById;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import com.gotta_watch_them_all.app.common.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.gotta_watch_them_all.app.helper.JsonHelper.jsonToObject;
import static com.gotta_watch_them_all.app.helper.JsonHelper.objectToJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private AuthHelperData userHelperData;

    @MockBean
    private SaveOneBannedWord mockSaveOneBannedWord;

    @MockBean
    private FindOneBannedWordById mockFindOneBannedWordById;

    @MockBean
    private FindAllBannedWords mockFindAllBannedWords;

    @MockBean
    private DeleteBannedWordById mockDeleteBannedWordById;

    @BeforeAll
    void initAll() {
        var adminRole = roleDao.findByRoleName(RoleName.ROLE_ADMIN);
        adminHelperData = authHelper.createUserAndGetAuthData("adminName", "admin@name.fr", "adminPassword", Set.of(adminRole));
        var userRole = roleDao.findByRoleName(RoleName.ROLE_USER);
        userHelperData = authHelper
                .createUserAndGetAuthData("username", "user@name.fr", "password", Set.of(userRole));
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
            mockMvc.perform(
                    post("/api/banned-word")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
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

            verify(mockSaveOneBannedWord, times(1)).execute("F word", false);
        }

        @Test
        void when_request_correct_and_has_attribute_update_comment_to_true_should_call_usecase_save_one_banned_word_and_updateComment_to_true() throws Exception {
            var saveBannedWordRequest = new SaveBannedWordRequest()
                    .setWord("F word");

            mockMvc.perform(
                    post("/api/banned-word?update_comment=true")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectToJson(saveBannedWordRequest)));

            verify(mockSaveOneBannedWord, times(1)).execute("F word", true);
        }

        @Test
        void when_usecase_throw_already_created_exception_should_send_forbidden_error_response() throws Exception {
            var saveBannedWordRequest = new SaveBannedWordRequest()
                    .setWord("F word");
            when(mockSaveOneBannedWord.execute("F word", false)).thenThrow(new AlreadyCreatedException("already created"));

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
            when(mockSaveOneBannedWord.execute("F word", false)).thenReturn(31L);

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

    @DisplayName("GET /api/banned-word/{id}")
    @Nested
    class FindByIdTest {
        @Test
        void when_user_not_authenticate_should_send_unauthorized_error_response() throws Exception {
            mockMvc.perform(
                    get("/api/banned-word/32")
            )
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @ValueSource(strings = {"notnumber", "-1", "2.3", "0"})
        void when_id_is_incorrect_should_send_bad_request_error_response(String incorrectId) throws Exception {
            mockMvc.perform(
                    get("/api/banned-word/" + incorrectId)
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void when_id_correct_should_call_usecase_findOneBannedWordById() throws Exception {
            mockMvc.perform(
                    get("/api/banned-word/34")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            );

            verify(mockFindOneBannedWordById, times(1)).execute(34L);
        }

        @Test
        void when_usecase_findOneBannedWordById_throw_NotFoundException_should_send_not_found_error_response() throws Exception {
            when(mockFindOneBannedWordById.execute(34L)).thenThrow(new NotFoundException("not found"));
            mockMvc.perform(
                    get("/api/banned-word/34")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            ).andExpect(status().isNotFound());
        }

        @Test
        void when_usecase_findOneBannedWordById_return_banned_word_should_return_found_banned_word() throws Exception {
            var bannedWord = new BannedWord()
                    .setId(34L)
                    .setWord("banned word");
            when(mockFindOneBannedWordById.execute(34L)).thenReturn(bannedWord);

            var contentAsString = mockMvc.perform(
                    get("/api/banned-word/34")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            var result = jsonToObject(contentAsString, BannedWord.class);

            assertThat(result).isEqualTo(bannedWord);
        }
    }

    @DisplayName("GET /api/banned-word")
    @Nested
    class FindAllTest {
        @Test
        void when_user_not_authenticate_should_unauthorized_error_response() throws Exception {
            mockMvc.perform(
                    get("/api/banned-word")
            )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void when_user_authenticate_should_call_usecase_findAllBannedWords() throws Exception {
            mockMvc.perform(
                    get("/api/banned-word")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            );

            verify(mockFindAllBannedWords, times(1)).execute();
        }

        @Test
        void when_usecase_return_set_banned_words_should_send_ok_response_with_set_banned_words() throws Exception {
            var setBannedWords = Set.of(
                    new BannedWord()
                            .setId(23L)
                            .setWord("bannedWord23"),
                    new BannedWord()
                            .setId(56L)
                            .setWord("bannedWord56")
            );
            when(mockFindAllBannedWords.execute()).thenReturn(setBannedWords);

            var contentAsString = mockMvc.perform(
                    get("/api/banned-word")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            ).andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(contentAsString).isNotBlank();
            var bannedWords = jsonToObject(contentAsString, BannedWord[].class);
            assertThat(bannedWords).isNotNull();
            assertThat(bannedWords.length).isGreaterThan(0);
            var result = new HashSet<>(Arrays.asList(bannedWords));
            assertThat(result).isEqualTo(setBannedWords);
        }
    }

    @DisplayName("DELETE /api/banned-word/{id}")
    @Nested
    class DeleteByIdTest {
        @Test
        void when_user_not_authorized_should_send_unauthorized_error_response() throws Exception {
            mockMvc.perform(
                    delete("/api/banned-word/123")
            ).andExpect(status().isUnauthorized());
        }

        @Test
        void when_user_is_not_admin_should_send_forbidden_error_response() throws Exception {
            mockMvc.perform(
                    delete("/api/banned-word/123")
                            .header("Authorization", "Bearer " + userHelperData.getJwtToken())
            ).andExpect(status().isForbidden());
        }

        @ParameterizedTest
        @ValueSource(strings = {"notnumber", "-2", "3.6", "0"})
        void when_banned_word_id_is_incorrect_should_send_bad_request_response(String incorrectBannedWord) throws Exception {
            mockMvc.perform(
                    delete("/api/banned-word/" + incorrectBannedWord)
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            ).andExpect(status().isBadRequest());
        }


        @Test
        void when_banned_word_id_is_correct_should_call_usecase_deleteBannedWordById() throws Exception {
            mockMvc.perform(
                    delete("/api/banned-word/76")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            );

            verify(mockDeleteBannedWordById, times(1)).execute(76L, false);
        }

        @Test
        void when_usecase_throw_not_found_exception_should_send_not_found_error_response() throws Exception {
            doThrow(new NotFoundException("not found")).when(mockDeleteBannedWordById).execute(76L, false);

            mockMvc.perform(
                    delete("/api/banned-word/76")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            ).andExpect(status().isNotFound());
        }

        @Test
        void when_usecase_do_nothing_should_send_not_content_success_response() throws Exception {
            doNothing().when(mockDeleteBannedWordById).execute(76L, false);

            mockMvc.perform(
                    delete("/api/banned-word/76")
                            .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
            ).andExpect(status().isNoContent());
        }
    }
}