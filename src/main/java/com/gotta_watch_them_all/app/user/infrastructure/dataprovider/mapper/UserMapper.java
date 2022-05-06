package com.gotta_watch_them_all.app.user.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.entity.UserEntity;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper.RoleMapper;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserEntity domainToEntity(User domain) {
        return new UserEntity()
                .setId(domain.getId())
                .setEmail(domain.getEmail())
                .setUsername(domain.getName())
                .setPassword(domain.getPassword())
                .setRoles(domain.getRoles()
                        .stream()
                        .map(RoleMapper::domainToEntity)
                        .collect(Collectors.toSet()))
                .setVulgar(domain.isVulgar());
    }

    public static User entityToDomain(UserEntity entity) {
        return new User()
                .setId(entity.getId())
                .setEmail(entity.getEmail())
                .setName(entity.getUsername())
                .setPassword(entity.getPassword())
                .setRoles(entity.getRoles()
                        .stream()
                        .map(RoleMapper::entityToDomain)
                        .collect(Collectors.toSet()))
                .setVulgar(entity.isVulgar());
    }
}
