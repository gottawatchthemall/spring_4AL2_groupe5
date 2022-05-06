package com.gotta_watch_them_all.app.role.usecase;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class  FindRoleByRoleName {
    private final RoleDao roleDao;

    public Role execute(RoleName roleName) {
        return roleDao.findByRoleName(roleName);
    }
}
