package com.gotta_watch_them_all.app.user.core.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AddUserEvent extends ApplicationEvent {
    private final String username;
    private final String email;
    private final String password;
    private final Set<String> strRoles;

    public AddUserEvent(
            Object source,
            String username,
            String email,
            String password,
            Set<String> strRoles
    ) {
        super(source);
        this.username = username;
        this.email = email;
        this.password = password;
        this.strRoles = strRoles;
    }
}
