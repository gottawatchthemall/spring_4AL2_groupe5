package com.gotta_watch_them_all.app.unit.role.usecase;

import com.gotta_watch_them_all.app.role.core.dao.RoleDao;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.role.usecase.AddRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddRoleTest {
    @Mock
    private RoleDao mockRoleDao;

    private AddRole sut;

    @BeforeEach
    void setup() {
        sut = new AddRole(mockRoleDao);
    }

    @Test
    void shouldReturnIdOfNewRole() {
        var roleName = RoleName.ROLE_ADMIN;
        var expectedId = 3L;
        when(mockRoleDao.createRole(roleName)).thenReturn(expectedId);

        var result = sut.execute(roleName);

        assertThat(result).isEqualTo(expectedId);
    }
}