package com.gotta_watch_them_all.app.e2e;

import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.helper.AuthHelper;
import com.gotta_watch_them_all.app.helper.AuthHelperData;
import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BannedWordApiTest {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private CommentDao commentDao;

    @LocalServerPort
    private int localPort;

    private AuthHelperData adminHelperData;

    @BeforeEach
    void setup() {
        port = localPort;
    }

    @BeforeAll
    void initAll() {
        var adminRole = roleDao.findByRoleName(RoleName.ROLE_ADMIN);
        adminHelperData = authHelper.createUserAndGetAuthData("username", "user@name.fr", "password", Set.of(adminRole));
    }

    @DisplayName("METHOD POST /api/banned-word and DELETE /api/banned-word/{id}")
    @Nested
    @DirtiesContext
    class MethodPostAndDeleteById {

        @DisplayName("when update_comment request param is true")
        @Nested
        class WhenUpdateCommentParamIsTrue {
            @Test
            void should_update_comment_vulgar_when_save_banned_word_and_or_delete_banned_word() {
                var commentToSave1 = new Comment()
                        .setContent("the testBannedWord guys are braves")
                        .setVulgar(false)
                        .setWorkId(12L)
                        .setUserId(adminHelperData.getUserId());
                var savedComment1 = commentDao.save(commentToSave1);
                var commentToSave2 = new Comment()
                        .setContent("future is kotlin style")
                        .setVulgar(false)
                        .setWorkId(13L)
                        .setUserId(adminHelperData.getUserId());
                var savedComment2 = commentDao.save(commentToSave2);
                var request = new SaveBannedWordRequest()
                        .setWord("testBannedWord");

                var responseUri = given()
                        .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/banned-word?update_comment=true")
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location");

                Set<Comment> comments = commentDao.findAll();
                assertThat(comments).isNotNull();
                var maybeComment1 = comments.stream().filter(comment -> comment.getId().equals(savedComment1.getId()))
                        .findFirst();
                assertThat(maybeComment1.isPresent()).isTrue();
                assertThat(maybeComment1.get().isVulgar()).isTrue();
                var maybeComment2 = comments.stream().filter(comment -> comment.getId().equals(savedComment2.getId()))
                        .findFirst();
                assertThat(maybeComment2.isPresent()).isTrue();
                assertThat(maybeComment2.get().isVulgar()).isFalse();

                // DELETE Banned word request
                given()
                        .header("Authorization", "Bearer " + adminHelperData.getJwtToken())
                        .when()
                        .delete(responseUri + "?update_comment=true")
                        .then()
                        .statusCode(204);

                comments = commentDao.findAll();
                assertThat(comments).isNotNull();
                maybeComment1 = comments.stream().filter(comment -> comment.getId().equals(savedComment1.getId()))
                        .findFirst();
                assertThat(maybeComment1.isPresent()).isTrue();
                assertThat(maybeComment1.get().isVulgar()).isFalse();
                maybeComment2 = comments.stream().filter(comment -> comment.getId().equals(savedComment2.getId()))
                        .findFirst();
                assertThat(maybeComment2.isPresent()).isTrue();
                assertThat(maybeComment2.get().isVulgar()).isFalse();
            }
        }
    }
}
