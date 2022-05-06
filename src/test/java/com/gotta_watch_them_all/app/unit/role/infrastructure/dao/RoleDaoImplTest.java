package com.gotta_watch_them_all.app.unit.role.infrastructure.dao;

import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.role.infrastructure.dao.RoleDaoImpl;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.entity.RoleEntity;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper.RoleMapper;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleDaoImplTest {

    @Mock
    private RoleRepository mockRoleRepository;

    private RoleDaoImpl sut;

    @BeforeEach
    void setup() {
        sut = new RoleDaoImpl(mockRoleRepository);
    }

    @Nested
    class CreateRole {
        @Test
        void shouldReturnIdOfSavedRole() {
            RoleName roleName = RoleName.ROLE_USER;
            RoleEntity roleEntity = new RoleEntity().setName(roleName);
            Long expectedId = 1L;

            when(mockRoleRepository.save(roleEntity))
                    .thenReturn(new RoleEntity().setId(expectedId).setName(roleName));

            var result = sut.createRole(roleName);

            assertThat(result).isEqualTo(expectedId);
        }
    }

    @Nested
    class FindByRoleName {

        @Test
        void shouldReturnFoundRole() {
            var roleUser = RoleName.ROLE_USER;
            var roleEntity = new RoleEntity().setName(roleUser);
            when(mockRoleRepository.findByName(roleUser)).thenReturn(Optional.of(roleEntity));
            var expected = new Role().setName(roleUser);

            var result = sut.findByRoleName(roleUser);

            assertThat(result).isEqualTo(expected);
        }

        @Test
        void whenRoleNotFound_shouldReturnNull() {
            var roleNotFound = RoleName.ROLE_USER;
            when(mockRoleRepository.findByName(roleNotFound)).thenReturn(Optional.empty());

            assertThat(sut.findByRoleName(roleNotFound)).isNull();
        }
    }

    @Nested
    class FindAll {
        @Test
        void should_call_findAll_of_roleRepository() {
            sut.findAll();
            verify(mockRoleRepository, times(1)).findAll();
        }

        @Test
        void should_return_list_of_role() {
            var userRoleEntity = new RoleEntity().setId(1L).setName(RoleName.ROLE_USER);
            var adminRoleEntity = new RoleEntity().setId(2L).setName(RoleName.ROLE_ADMIN);
            var roleEntities = Arrays.asList(userRoleEntity, adminRoleEntity);

            when(mockRoleRepository.findAll()).thenReturn(roleEntities);
            var expected = roleEntities.stream().map(RoleMapper::entityToDomain)
                    .collect(Collectors.toList());

            var result = sut.findAll();

            assertThat(result).isEqualTo(expected);
        }
    }
}