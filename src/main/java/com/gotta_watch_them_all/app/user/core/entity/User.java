package com.gotta_watch_them_all.app.user.core.entity;

import com.gotta_watch_them_all.app.role.core.entity.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Set<Role> roles;
    private boolean vulgar;
}
