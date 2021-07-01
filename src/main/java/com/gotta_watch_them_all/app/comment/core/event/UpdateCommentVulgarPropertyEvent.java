package com.gotta_watch_them_all.app.comment.core.event;

import org.springframework.context.ApplicationEvent;

public class UpdateCommentVulgarPropertyEvent extends ApplicationEvent {
    public UpdateCommentVulgarPropertyEvent(Object source) {
        super(source);
    }
}
