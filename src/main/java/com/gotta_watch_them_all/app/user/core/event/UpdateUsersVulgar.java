package com.gotta_watch_them_all.app.user.core.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@ToString
public class UpdateUsersVulgar extends ApplicationEvent {
    private final Set<Long> setUserId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public UpdateUsersVulgar(Object source, Set<Long> setUserId) {
        super(source);
        this.setUserId = setUserId;
    }
}
