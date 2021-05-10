package com.gotta_watch_them_all.app.role.core.dao;

import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;

import java.util.List;

public interface RoleDao {
    Long createRole(RoleName roleName);

    Role findByRoleName(RoleName roleName);

    List<Role> findAll();
}
