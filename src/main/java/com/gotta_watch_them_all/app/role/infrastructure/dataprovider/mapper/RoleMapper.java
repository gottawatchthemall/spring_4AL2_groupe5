package com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.entity.RoleEntity;

public class RoleMapper {
    public static Role entityToDomain(RoleEntity entity) {
        return new Role()
                .setId(entity.getId())
                .setName(entity.getName());
    }

    public static RoleEntity domainToEntity(Role role) {
        return new RoleEntity()
                .setId(role.getId())
                .setName(role.getName());
    }
}
