package com.gotta_watch_them_all.app.unit.user.infrastructure.dao;

import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user.infrastructure.dao.UserDaoImpl;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.entity.UserEntity;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper.RoleMapper;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.mapper.UserMapper;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockEncoder;

    private UserDaoImpl sut;
    private MockedStatic<UserMapper> mockUserMapper;

    @BeforeEach
    void setup() {
        sut = new UserDaoImpl(mockUserRepository, mockEncoder);
    }

    @Test
    void createUser_return_id_of_saved_user() {
        var roles = Set.of(new Role().setName(RoleName.ROLE_USER));
        var userEntity = new UserEntity()
                .setUsername("user name")
                .setEmail("user@email.com")
                .setPassword("passwordEncoded")
                .setRoles(roles.stream().map(RoleMapper::domainToEntity).collect(Collectors.toSet()));
        when(mockEncoder.encode("password")).thenReturn("passwordEncoded");
        when(mockUserRepository.save(userEntity)).thenReturn(new UserEntity().setId(5L));

        var result = sut.createUser("user name", "user@email.com", "password", roles);

        assertThat(result).isEqualTo(5L);
    }

    @Test
    public void findById_should_throw_not_found_exception_if_id_null() {
        assertThrows(NotFoundException.class, () -> sut.findById(null));
    }

    @Test
    public void findById_should_call_user_repository_once() throws NotFoundException {
        var roles = Set.of(new Role().setName(RoleName.ROLE_USER));
        var userEntity = new UserEntity()
                .setUsername("user name")
                .setEmail("user@email.com")
                .setPassword("passwordEncoded")
                .setRoles(roles.stream().map(RoleMapper::domainToEntity).collect(Collectors.toSet()));
        when(mockUserRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userEntity));
        sut.findById(1L);
        verify((mockUserRepository), times(1)).findById(1L);
    }

    @Test
    public void findById_should_throw_not_found_exception_if_not_found() {
        when(mockUserRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sut.findById(47L));
    }

    @Test
    public void findById_should_return_user_with_right_id() throws NotFoundException {
        var roles = Set.of(new Role().setName(RoleName.ROLE_USER));
        var expectedEntity = new UserEntity()
                .setUsername("user name")
                .setEmail("user@email.com")
                .setPassword("passwordEncoded")
                .setRoles(roles.stream().map(RoleMapper::domainToEntity).collect(Collectors.toSet()));
        var expectedUser = new User()
                .setName("user name")
                .setId(1L)
                .setEmail("user@email.com")
                .setPassword("passwordEncoded")
                .setRoles(roles);
        when(mockUserRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedEntity));
        try (MockedStatic<UserMapper> mapper = Mockito.mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.entityToDomain(expectedEntity))
                    .thenReturn(expectedUser);

            var user = sut.findById(1L);
            assertEquals(expectedUser, user);
        }

    }
}
