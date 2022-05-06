package com.gotta_watch_them_all.app.unit.auth.usecase;

import com.gotta_watch_them_all.app.auth.usecase.SignUp;
import com.gotta_watch_them_all.app.user.core.event.AddUserEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignUpTest {
    private SignUp sut;

    @Mock
    private AddUserEventPublisher mockPublisher;

    @BeforeEach
    void setup() {
        sut = new SignUp(mockPublisher);
    }

    @Test
    void should_publish_params_to_add_user_event_publisher_with_user_str_role() {
        var username = "name";
        var email = "user@gmail.com";
        var password = "password";
        sut.execute(username, email, password);

        verify(mockPublisher, times(1)).publishEvent(
                username,
                email,
                password,
                Set.of("user")
        );
    }
}
