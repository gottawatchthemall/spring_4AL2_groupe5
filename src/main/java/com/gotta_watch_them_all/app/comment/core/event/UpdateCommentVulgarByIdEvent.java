package com.gotta_watch_them_all.app.comment.core.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class UpdateCommentVulgarByIdEvent extends ApplicationEvent {
    private final Long commentId;

    public UpdateCommentVulgarByIdEvent(Object source, Long commentId) {
        super(source);
        this.commentId = commentId;
    }
}
