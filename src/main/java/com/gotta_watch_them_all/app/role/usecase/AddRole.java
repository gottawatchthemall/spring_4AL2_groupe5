package com.gotta_watch_them_all.app.role.usecase;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddRole {
    private final RoleDao roleDao;

    public Long execute(RoleName roleName) {
        return roleDao.createRole(roleName);
    }
}
