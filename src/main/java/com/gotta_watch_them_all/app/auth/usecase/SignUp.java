package com.gotta_watch_them_all.app.auth.usecase;

import com.gotta_watch_them_all.app.common.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.event.AddUserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SignUp {
    private final AddUserEventPublisher addUserEventPublisher;

    public void execute(
            String username,
            String email,
            String password
    ) throws AlreadyCreatedException, NotFoundException {
        addUserEventPublisher.publishEvent(username, email, password, Set.of("user"));
    }

}
