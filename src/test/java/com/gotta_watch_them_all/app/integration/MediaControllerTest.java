package com.gotta_watch_them_all.app.integration;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.helper.AuthHelper;
import com.gotta_watch_them_all.app.media.infrastructure.entrypoint.MediaAdapter;
import com.gotta_watch_them_all.app.media.infrastructure.entrypoint.CreateMediaRequest;
import com.gotta_watch_them_all.app.media.infrastructure.entrypoint.MediaResponse;
import com.gotta_watch_them_all.app.media.usecase.AddMedia;
import com.gotta_watch_them_all.app.media.usecase.DeleteMedia;
import com.gotta_watch_them_all.app.media.usecase.FindAllMedias;
import com.gotta_watch_them_all.app.media.usecase.FindMediaById;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gotta_watch_them_all.app.helper.JsonHelper.jsonToObject;
import static com.gotta_watch_them_all.app.helper.JsonHelper.objectToJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MediaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private RoleDao roleDao;

    @MockBean
    private FindAllMedias mockFindAllMedias;

    @MockBean
    private AddMedia mockAddMedia;

    @MockBean
    private FindMediaById mockFindMediaById;

    @MockBean
    private DeleteMedia mockDeleteMedia;

    private String jwtAdmin;

    @BeforeAll
    void initAll() {
        var adminRole = roleDao.findByRoleName(RoleName.ROLE_ADMIN);
        var adminHelperData = authHelper.createUserAndGetJwt("username", "user@name.fr", "password", Set.of(adminRole));
        jwtAdmin = adminHelperData.getJwtToken();
    }

    @DisplayName("GET /api/media")
    @Nested
    class FindAll {
        @Test
        void when_call_findAllMedias_should_return_list_media() throws Exception {
            var filmMedia = new Media().setId(1L).setName("film");
            var seriesMedia = new Media().setId(2L).setName("series");
            var mediaList = Arrays.asList(filmMedia, seriesMedia);
            var mediaResponseList = mediaList.stream()
                    .map(MediaAdapter::domainToResponse)
                    .collect(Collectors.toList());

            when(mockFindAllMedias.execute()).thenReturn(mediaList);

            var contentAsString = mockMvc.perform(
                    get("/api/media")
            )
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var result = Arrays.asList(Objects.requireNonNull(jsonToObject(contentAsString, MediaResponse[].class)));

            assertThat(result).isEqualTo(mediaResponseList);
        }
    }

    @DisplayName("GET /api/media/{id}")
    @Nested
    class FindById {
        @ParameterizedTest
        @ValueSource(strings = {"notNumber", "2.3"})
        void when_request_param_is_not_integer_should_send_error_response(String notCorrectId) throws Exception {
            var errorContent = mockMvc.perform(get("/api/media/" + notCorrectId))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertThat(errorContent).contains("id has to be an integer");
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "0"})
        void when_request_param_is_less_than_1_should_send_error_response(String notCorrectId) throws Exception {
            var errorContent = mockMvc.perform(get("/api/media/" + notCorrectId))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertThat(errorContent).contains("id has to be equal or more than 1");
        }

        @Test
        void when_findOneMedia_throw_NotFoundException_should_send_not_found_response() throws Exception {
            var mediaId = 1L;
            var message = String.format("Media with '%d' not found", mediaId);
            when(mockFindMediaById.execute(mediaId)).thenThrow(new NotFoundException(message));
            var errorContent = mockMvc.perform(get("/api/media/" + mediaId))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertThat(errorContent).isEqualTo(message);
        }

        @Test
        void when_findOneMedia_return_one_media_should_send_success_response() throws Exception {
            var mediaId = 1L;
            var foundMedia = new Media().setId(mediaId).setName("film");
            var mediaResponse = MediaAdapter.domainToResponse(foundMedia);

            when(mockFindMediaById.execute(mediaId)).thenReturn(foundMedia);
            var contentAsString = mockMvc.perform(get("/api/media/" + mediaId))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var response = jsonToObject(contentAsString, MediaResponse.class);
            assertThat(response).isEqualTo(mediaResponse);
        }
    }

    @DisplayName("POST /api/media")
    @Nested
    class CreateOne {
        @ParameterizedTest
        @ValueSource(strings = {"", "       ", "\n", "\t"})
        public void when_name_not_correct_should_response_bad_request(String notCorrectName) throws Exception {
            CreateMediaRequest mediaRequest = new CreateMediaRequest();
            mediaRequest.setName(notCorrectName);
            var content = mockMvc.perform(post("/api/media").header("Authorization", "Bearer " + jwtAdmin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectToJson(mediaRequest)))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(content).isEqualTo("{\"name\":\"Name has to be not blank\"}");
        }

        @Test
        public void when_add_media_throw_AlreadyCreatedException_should_response_bad_request_with_message() throws Exception {
            String name = "film";
            CreateMediaRequest mediaRequest = new CreateMediaRequest();
            mediaRequest.setName(name);

            String message = "Media with name 'film' already created";

            when(mockAddMedia.execute(name)).thenThrow(new AlreadyCreatedException(message));

            var content = mockMvc.perform(post("/api/media").header("Authorization", "Bearer " + jwtAdmin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectToJson(mediaRequest)))
                    .andExpect(status().isForbidden())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(content).isEqualTo(message);
        }

        @Test
        public void when_media_is_add_should_success_response_with_link_to_new_media_in_header_location() throws Exception {
            CreateMediaRequest mediaRequest = new CreateMediaRequest();
            mediaRequest.setName("film");
            when(mockAddMedia.execute("film")).thenReturn(1L);

            var location = mockMvc.perform(post("/api/media").header("Authorization", "Bearer " + jwtAdmin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectToJson(mediaRequest)))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getHeader("Location");

            var response = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/api/media/{id}")
                    .buildAndExpand("1")
                    .toUri();
            assertThat(location).isEqualTo(response.toString());
        }
    }

    @DisplayName("DELETE /api/media")
    @Nested
    class DeleteOne {

        @ParameterizedTest
        @ValueSource(strings = {"notnumber", "2.3"})
        public void when_param_is_not_number_should_response_bad_request(String notNumber) throws Exception {
            var errorContent = mockMvc.perform(delete("/api/media/" + notNumber).header("Authorization", "Bearer " + jwtAdmin))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertThat(errorContent).contains("id has to be an integer");
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "0"})
        public void when_param_is_less_than_1_should_send_bad_request_response(String lessThan1) throws Exception {
            var errorContent = mockMvc.perform(delete("/api/media/" + lessThan1).header("Authorization", "Bearer " + jwtAdmin))
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertThat(errorContent).contains("deleteOne.mediaId: id has to be equal or more than 1");
        }

        @Test
        public void when_deleteMedia_throw_NotFoundException_should_send_bad_request_response() throws Exception {
            var mediaId = 1L;
            var message = String.format("Media with id '%d' not found", mediaId);
            doThrow(new NotFoundException(message)).when(mockDeleteMedia).execute(mediaId);
            var errorContent = mockMvc.perform(delete("/api/media/" + mediaId).header("Authorization", "Bearer " + jwtAdmin))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            assertThat(errorContent).isEqualTo(message);
        }

        @Test
        public void when_media_delete_should_send_success_noContent_response() throws Exception {
            var mediaId = 1L;
            doNothing().when(mockDeleteMedia).execute(mediaId);
            mockMvc.perform(delete("/api/media/" + mediaId).header("Authorization", "Bearer " + jwtAdmin))
                    .andExpect(status().isNoContent());
        }
    }
}