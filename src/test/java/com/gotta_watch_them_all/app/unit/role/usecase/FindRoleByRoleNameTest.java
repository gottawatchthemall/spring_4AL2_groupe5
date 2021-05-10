package com.gotta_watch_them_all.app.unit.role.usecase;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.role.usecase.FindRoleByRoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRoleByRoleNameTest {
    @Mock
    private RoleDao mockRoleDao;

    private FindRoleByRoleName sut;

    @BeforeEach
    void setup() {
        sut = new FindRoleByRoleName(mockRoleDao);
    }

    @Test
    void shouldReturnConcernedRole() {
        var roleUser = RoleName.ROLE_USER;
        var role = new Role().setId(1L).setName(roleUser);
        when(mockRoleDao.findByRoleName(roleUser)).thenReturn(role);

        var result = sut.execute(roleUser);

        assertThat(result).isEqualTo(role);
    }
}