package com.gotta_watch_them_all.app.e2e;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.auth.infrastructure.entrypoint.LoginRequest;
import com.gotta_watch_them_all.app.auth.infrastructure.entrypoint.SignupRequest;
import com.gotta_watch_them_all.app.auth.infrastructure.entrypoint.JwtResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Collections;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthApiTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @LocalServerPort
    private int localPort;

    @BeforeEach
    void setup() {
        port = localPort;
    }

    @DisplayName("METHOD POST /api/auth/signup")
    @Nested
    class SignUp {

        @Test
        void when_request_is_correct_should_send_success_response() {
            var signupRequest = new SignupRequest()
                    .setUsername("new user name")
                    .setEmail("newuser@email.com")
                    .setPassword("newuserpassword");
            given()
                    .contentType(ContentType.JSON)
                    .body(signupRequest)
                    .when()
                    .post("/api/auth/signup")
                    .then()
                    .statusCode(200)
                    .extract()
                    .asString();

            assertThat(userDao.existsByUsername(signupRequest.getUsername())).isTrue();
            assertThat(userDao.existsByEmail(signupRequest.getEmail())).isTrue();
        }
    }

    @DisplayName("METHOD POST /api/auth/signin")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SignIn {
        private final String userName = "user name";
        private final String password = "password";
        private final String email = "user@email.com";

        @BeforeAll
        void initAll() {
            var userRole = roleDao.findByRoleName(RoleName.ROLE_USER);
            userDao.createUser(userName, email, password, Set.of(userRole));
        }

        @Test
        void when_request_is_correct_should_send_jwt_response() {
            var loginRequest = new LoginRequest()
                    .setUsername(userName)
                    .setPassword(password);
            var expectedJwt = new JwtResponse()
                    .setUsername(userName)
                    .setEmail(email)
                    .setRoles(Collections.singletonList("ROLE_USER"));

            var jwtResponse = given()
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                    .when()
                    .post("/api/auth/signin")
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(JwtResponse.class);

            assertThat(jwtResponse.getId()).isNotNull();
            assertThat(jwtResponse.getToken()).isNotNull();
            assertThat(jwtResponse.getRoles()).isEqualTo(expectedJwt.getRoles());
            assertThat(jwtResponse.getUsername()).isEqualTo(expectedJwt.getUsername());
            assertThat(jwtResponse.getEmail()).isEqualTo(expectedJwt.getEmail());
        }
    }
}
