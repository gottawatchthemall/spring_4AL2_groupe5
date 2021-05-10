package com.gotta_watch_them_all.app.role.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Role {
    private Long id;

    private RoleName name;
}
