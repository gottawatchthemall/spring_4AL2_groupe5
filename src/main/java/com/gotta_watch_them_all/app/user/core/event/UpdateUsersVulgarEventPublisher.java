package com.gotta_watch_them_all.app.user.core.event;

import java.util.Set;

public interface UpdateUsersVulgarEventPublisher {
    void publishEvent(Set<Long> setUserId);
}
