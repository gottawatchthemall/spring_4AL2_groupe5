package com.gotta_watch_them_all.app.user.core.event;

import java.util.Set;

public interface AddUserEventPublisher {
    void publishEvent(
            String username,
            String email,
            String password,
            Set<String> strRoles
    );
}
