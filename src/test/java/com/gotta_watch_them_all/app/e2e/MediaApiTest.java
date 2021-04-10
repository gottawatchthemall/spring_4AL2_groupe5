package com.gotta_watch_them_all.app.e2e;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.entity.MediaEntity;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.entity.mapper.MediaMapper;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.repository.MediaRepository;
import com.gotta_watch_them_all.app.infrastructure.entrypoint.request.CreateMediaRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MediaApiTest {
    @Autowired
    private MediaRepository mediaRepository;

    @LocalServerPort
    private int localPort;

    @BeforeEach
    void setup() {
        port = localPort;
        //filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @DisplayName("METHOD GET /media")
    @Nested
    class FindAllMedias {
        @Test
        void should_get_all_medias() {
            mediaRepository.deleteAll();
            MediaEntity filmMedia = MediaEntity.builder().name("film").build();
            MediaEntity seriesMedia = MediaEntity.builder().name("series").build();
            var mediaEntityList = Arrays.asList(filmMedia, seriesMedia);
            mediaRepository.saveAll(mediaEntityList);

            var expectedResponse = mediaEntityList.stream()
                    .map(MediaMapper::entityToDomain)
                    .collect(Collectors.toList());

            List<Media> response = given()
                    .when()
                    .get("/api/media")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList(".", Media.class);

            assertThat(response).isNotNull();
            assertThat(response.size()).isEqualTo(expectedResponse.size());
            assertThat(response).isEqualTo(expectedResponse);
        }
    }

    @DisplayName("METHOD GET /media/{id}")
    @Nested
    class FindOneMedia {
        @ParameterizedTest
        @ValueSource(strings = {"notNumber", "1.0", "0", "-1"})
        void when_id_is_not_integer_more_than_zero_should_get_error_response(String notCorrectId) {
            given()
                    .when()
                    .get("/api/media/" + notCorrectId)
                    .then()
                    .statusCode(400);
        }

        @Test
        void when_id_correspond_to_one_media_should_return_concerned_media() {
            MediaEntity filmMedia = MediaEntity.builder().name("film").build();
            MediaEntity seriesMedia = MediaEntity.builder().name("series").build();
            MediaEntity mangaMedia = MediaEntity.builder().name("manga").build();
            var mediaEntityList = Arrays.asList(filmMedia, seriesMedia, mangaMedia);
            var savedMediaEntityList = mediaRepository.saveAll(mediaEntityList);

            var expectMedia = MediaMapper.entityToDomain(savedMediaEntityList.get(1));
            var response = given()
                    .when()
                    .get("/api/media/" + expectMedia.getId())
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(Media.class);

            assertThat(response).isEqualTo(expectMedia);
        }

        @Test
        void when_id_not_correspond_to_one_media_should_return_error_response() {
            MediaEntity filmMedia = MediaEntity.builder().name("film").build();
            MediaEntity seriesMedia = MediaEntity.builder().name("series").build();
            MediaEntity mangaMedia = MediaEntity.builder().name("manga").build();
            var mediaEntityList = Arrays.asList(filmMedia, seriesMedia, mangaMedia);
            var savedMediaEntityList = mediaRepository.saveAll(mediaEntityList);

            var expectMedia = MediaMapper.entityToDomain(savedMediaEntityList.get(1));
            mediaRepository.deleteById(expectMedia.getId());

            var response = given()
                    .when()
                    .get("/api/media/" + expectMedia.getId())
                    .then()
                    .statusCode(404)
                    .extract()
                    .asString();

            assertThat(response).isEqualTo("Media with id '" + expectMedia.getId() + "' not found");
        }
    }

    @DisplayName("METHOD POST /media")
    @Nested
    class CreateMedia {
        // TODO : check if auth user is admin

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\n", "\t"})
        void when_name_is_blank_send_error_response(String blankName) {
            var request = new CreateMediaRequest();
            request.setName(blankName);

            var response = given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/media")
                    .then()
                    .statusCode(400)
                    .extract()
                    .asString();
            assertThat(response).contains("{\"name\":\"Name has to be not blank\"}");
        }

        @DirtiesContext
        @Test
        public void when_name_is_correct_should_create_media_and_return_uri() {
            CreateMediaRequest request = new CreateMediaRequest();
            request.setName("series");
            var response = given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/media")
                    .then()
                    .statusCode(201)
                    .extract()
                    .header("Location");
            var checkCreatedMedia = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(response)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(Media.class);
            assertThat(checkCreatedMedia.getId()).isNotNull().isInstanceOf(Long.class);
            assertThat(checkCreatedMedia.getName()).isEqualTo(request.getName());
        }

        @DirtiesContext
        @Test
        public void when_name_already_exist_should_send_error_response() {
            mediaRepository.save(MediaEntity.builder().name("series").build());
            CreateMediaRequest request = new CreateMediaRequest();
            request.setName("series");

            var response = given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/media")
                    .then()
                    .statusCode(403)
                    .extract()
                    .asString();
            assertThat(response).isEqualTo("Media with name 'series' is already created");
        }
    }

    @DisplayName("METHOD DELETE /media/{id}")
    @Nested
    class DeleteMedia {
        @ParameterizedTest
        @ValueSource(strings = {"notId", "0", "-1", "2.1"})
        void when_id_not_correct_should_send_error_response(String notCorrectId) {
            when()
                    .delete("/api/media/" + notCorrectId)
                    .then()
                    .statusCode(400);
        }

        @Test
        void when_id_not_correspond_to_media_id_database_should_send_error_response() {
            var mediaToSave = MediaEntity.builder()
                    .name("film")
                    .build();
            var mediaToDelete = mediaRepository.save(mediaToSave);
            mediaRepository.deleteById(mediaToDelete.getId());
            var response = when()
                    .delete("/api/media/" + mediaToDelete.getId())
                    .then()
                    .statusCode(404)
                    .extract()
                    .asString();
            assertThat(response).isEqualTo("Media with id '" + mediaToDelete.getId() + "' not found");
        }

        @DirtiesContext
        @Test
        void when_media_with_certain_id_found_should_delete_concerned_media() {
            var mediaToSave = MediaEntity.builder()
                    .name("film")
                    .build();
            var mediaToDelete = mediaRepository.save(mediaToSave);

            when()
                    .delete("/api/media/" + mediaToDelete.getId())
                    .then()
                    .statusCode(204);

            var maybeMedia = mediaRepository.findById(mediaToDelete.getId());

            assertThat(maybeMedia.isPresent()).isFalse();
        }
    }
}
