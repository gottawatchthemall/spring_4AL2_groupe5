package com.gotta_watch_them_all.app.user.core.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class UpdateUsersVulgarEvent extends ApplicationEvent {
    private final Set<Long> setUserId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param setUserId
     */
    public UpdateUsersVulgarEvent(Object source, Set<Long> setUserId) {
        super(source);
        this.setUserId = setUserId;
    }
}
