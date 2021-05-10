package com.gotta_watch_them_all.app.role.infrastructure.bootstrap;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleBootstrap {
    private final RoleDao roleDao;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var roleNames = Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_ADMIN);
        var result = getAllSavedRoleNames();

        createNotSavedRoles(roleNames, result);
    }

    private List<RoleName> getAllSavedRoleNames() {
        return roleDao.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    private void createNotSavedRoles(List<RoleName> roleNames, List<RoleName> result) {
        roleNames.stream()
                .filter(roleName -> !result.contains(roleName))
                .forEach(roleDao::createRole);
    }
}
