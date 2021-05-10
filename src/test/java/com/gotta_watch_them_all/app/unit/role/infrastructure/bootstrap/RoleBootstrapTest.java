package com.gotta_watch_them_all.app.unit.role.infrastructure.bootstrap;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.role.infrastructure.bootstrap.RoleBootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleBootstrapTest {
    @Mock
    private ApplicationReadyEvent mockApplicationReadyEvent;

    @Mock
    private RoleDao mockRoleDao;

    private RoleBootstrap sut;

    @BeforeEach
    void setup() {
        sut = new RoleBootstrap(mockRoleDao);
    }

    @Test
    void on_shouldCallFindByNameWithRolesUserAndAdminOfRepository() {
        sut.on(mockApplicationReadyEvent);

        verify(mockRoleDao, times(1)).findAll();
    }

    @Test
    void on_whenRolesUserAndAdminAreAlreadySaved_shouldNotSaveRole() {
        var userRole = new Role().setId(1L).setName(RoleName.ROLE_USER);
        var adminRole = new Role().setId(2L).setName(RoleName.ROLE_ADMIN);

        when(mockRoleDao.findAll()).thenReturn(Arrays.asList(userRole, adminRole));

        sut.on(mockApplicationReadyEvent);

        verify(mockRoleDao, never()).createRole(any(RoleName.class));
    }

    @Test
    void on_whenNoRoleIsSaved_shouldSaveUserAndAdminRoles() {
        when(mockRoleDao.findAll()).thenReturn(new ArrayList<>());

        sut.on(mockApplicationReadyEvent);

        verify(mockRoleDao, times(1)).createRole(RoleName.ROLE_USER);
        verify(mockRoleDao, times(1)).createRole(RoleName.ROLE_ADMIN);
    }

    @Test
    void on_whenOnlyUserRoleIsSaved_shouldSaveAdminRole() {
        var userRole = new Role().setId(1L).setName(RoleName.ROLE_USER);
        when(mockRoleDao.findAll()).thenReturn(Collections.singletonList(userRole));

        sut.on(mockApplicationReadyEvent);

        verify(mockRoleDao, times(1)).createRole(RoleName.ROLE_ADMIN);
    }
}