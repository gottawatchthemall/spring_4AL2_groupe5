package com.gotta_watch_them_all.app.comment.core.event;

import org.springframework.context.ApplicationEvent;

public class UpdateCommentsVulgarEvent extends ApplicationEvent {
    public UpdateCommentsVulgarEvent(Object source) {
        super(source);
    }
}
