package com.gotta_watch_them_all.app.user.infrastructure.dataprovider.event.publisher;

import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateUsersVulgarEventPublisherImpl implements UpdateUsersVulgarEventPublisher {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publishEvent(Set<Long> setUserId) {
        publisher.publishEvent(new UpdateUsersVulgarEvent(this, setUserId));
    }
}
