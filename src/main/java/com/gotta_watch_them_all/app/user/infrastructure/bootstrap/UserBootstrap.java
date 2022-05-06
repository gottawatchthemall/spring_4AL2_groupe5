package com.gotta_watch_them_all.app.user.infrastructure.bootstrap;

import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.event.AddUserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserBootstrap {
    private final UserDao userDao;
    private final AddUserEventPublisher publisher;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var users = userDao.findAll();

        if (users.isEmpty()) {
            publisher.publishEvent(
                    "root",
                    "root@root.com",
                    "rootpassword",
                    Set.of("admin")
            );
        }
    }
}
