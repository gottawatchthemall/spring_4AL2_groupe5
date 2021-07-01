package com.gotta_watch_them_all.app.comment.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UpdateCommentsVulgarEventPublisherImpl implements UpdateCommentsVulgarEventPublisher {
    @Override
    public void publishEvent() {

    }
}
