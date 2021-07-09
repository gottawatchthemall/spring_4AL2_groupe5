package com.gotta_watch_them_all.app.user.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.user.core.event.AddUserEvent;
import com.gotta_watch_them_all.app.user.core.event.AddUserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddUserEventPublisherImpl implements AddUserEventPublisher {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(String username, String email, String password, Set<String> strRoles) {
        var event = new AddUserEvent(
                this,
                username,
                email,
                password,
                strRoles
        );
        publisher.publishEvent(event);
    }
}
