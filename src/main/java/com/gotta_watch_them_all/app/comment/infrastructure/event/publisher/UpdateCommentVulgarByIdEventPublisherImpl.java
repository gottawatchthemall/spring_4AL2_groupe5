package com.gotta_watch_them_all.app.comment.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCommentVulgarByIdEventPublisherImpl implements UpdateCommentVulgarByIdEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(Long commentId) {
        applicationEventPublisher.publishEvent(new UpdateCommentVulgarByIdEvent(this, commentId));
    }
}
