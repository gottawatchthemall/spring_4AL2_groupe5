package com.gotta_watch_them_all.app.role.infrastructure.dao;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.entity.RoleEntity;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper.RoleMapper;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleDaoImpl implements RoleDao {
    private final RoleRepository roleRepository;

    @Override
    public Long createRole(RoleName roleName) {
        var savedRoleEntity = roleRepository.save(new RoleEntity().setName(roleName));
        return savedRoleEntity.getId();
    }

    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .map(RoleMapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::entityToDomain)
                .collect(Collectors.toList());
    }
}
