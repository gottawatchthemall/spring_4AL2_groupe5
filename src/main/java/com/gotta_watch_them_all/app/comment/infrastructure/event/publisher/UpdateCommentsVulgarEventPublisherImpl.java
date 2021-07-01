package com.gotta_watch_them_all.app.comment.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCommentsVulgarEventPublisherImpl implements UpdateCommentsVulgarEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void publishEvent() {
        applicationEventPublisher.publishEvent(new UpdateCommentsVulgarEvent(this));
    }
}
