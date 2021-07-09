package com.gotta_watch_them_all.app.comment.core.event;

public interface UpdateCommentVulgarByIdEventPublisher {
    void publishEvent(Long commentId);
}
