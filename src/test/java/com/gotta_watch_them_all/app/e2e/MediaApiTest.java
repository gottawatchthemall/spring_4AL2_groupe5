package com.gotta_watch_them_all.app.e2e;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.core.dao.RoleDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.core.entity.RoleName;
import com.gotta_watch_them_all.app.helper.AuthHelper;
import com.gotta_watch_them_all.app.media.infrastructure.entrypoint.CreateMediaRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MediaApiTest {

    @Autowired
    private MediaDao mediaRepository;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private RoleDao roleDao;

    @LocalServerPort
    private int localPort;

    private String jwtAdmin;

    @BeforeEach
    void setup() {
        port = localPort;
    }

    @BeforeAll
    void initAll() {
        var adminRole = roleDao.findByRoleName(RoleName.ROLE_ADMIN);
        jwtAdmin = authHelper.createUserAndGetJwt("username", "user@name.fr", "password", Set.of(adminRole));
    }

    @DisplayName("METHOD GET /api/media")
    @Nested
    class FindAllMedias {
        @Test
        void should_get_all_medias() {
            mediaRepository.deleteAll();
            Media filmMedia = new Media().setName("film");
            Media seriesMedia = new Media().setName("series");
            var mediaList = Arrays.asList(filmMedia, seriesMedia);
            var expectedList = mediaRepository.saveAll(mediaList);

            List<Media> response = given()
                    .when()
                    .get("/api/media")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList(".", Media.class);

            assertThat(response).isNotNull();
            assertThat(response.size()).isEqualTo(expectedList.size());
            assertThat(response).isEqualTo(expectedList);
        }
    }

    @DisplayName("METHOD GET /api/media/{id}")
    @Nested
    class FindMediaById {
        @Test
        void when_id_correspond_to_one_media_should_return_concerned_media() {
            Media filmMedia = new Media().setName("film");
            Media seriesMedia = new Media().setName("series");
            Media mangaMedia = new Media().setName("manga");
            var mediaList = Arrays.asList(filmMedia, seriesMedia, mangaMedia);
            var savedMediaList = mediaRepository.saveAll(mediaList);

            var expectMedia = savedMediaList.get(1);
            var response = given()
                    .when()
                    .get("/api/media/" + expectMedia.getId())
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(Media.class);

            assertThat(response).isEqualTo(expectMedia);
        }
    }

    @DisplayName("METHOD POST /api/media")
    @Nested
    class CreateMedia {
        // TODO : check if auth user is admin

        @Test
        public void when_name_is_correct_should_create_media_and_return_uri() {
            CreateMediaRequest request = new CreateMediaRequest();
            request.setName("series");
            var response = given()
                    .header("Authorization", "Bearer " + jwtAdmin)
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

        @Test
        public void when_name_already_exist_should_send_error_response() {
            mediaRepository.createMedia("series2");
            CreateMediaRequest request = new CreateMediaRequest();
            request.setName("series2");

            var response = given()
                    .header("Authorization", "Bearer " + jwtAdmin)
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/media")
                    .then()
                    .statusCode(403)
                    .extract()
                    .asString();
            assertThat(response).isEqualTo("Media with name 'series2' is already created");
        }
    }

    @DisplayName("METHOD DELETE /api/media/{id}")
    @Nested
    class DeleteMedia {
        @Test
        void when_media_with_certain_id_found_should_delete_concerned_media() {
            var mediaToDelete = mediaRepository.createMedia("film");

            given()
                    .header("Authorization", "Bearer " + jwtAdmin)
                    .when()
                    .delete("/api/media/" + mediaToDelete)
                    .then()
                    .statusCode(204);

            var maybeMedia = mediaRepository.findById(mediaToDelete);

            assertThat(maybeMedia).isNull();
        }
    }
}
