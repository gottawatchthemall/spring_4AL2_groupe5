package com.gotta_watch_them_all.app.user.infrastructure.event.listener;

import com.gotta_watch_them_all.app.user.core.event.AddUserEvent;
import com.gotta_watch_them_all.app.user.usecase.AddUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddUserEventListener implements ApplicationListener<AddUserEvent> {
    private final AddUser addUser;

    @Override
    public void onApplicationEvent(AddUserEvent event) {
        addUser.execute(
                event.getUsername(),
                event.getEmail(),
                event.getPassword(),
                event.getStrRoles()
        );
    }
}
