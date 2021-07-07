package com.gotta_watch_them_all.app.unit.user.infrastructure.dao;

import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.entity.RoleEntity;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper.RoleMapper;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user.infrastructure.dao.UserDaoImpl;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.entity.UserEntity;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.mapper.UserMapper;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockEncoder;

    private UserDaoImpl sut;

    @BeforeEach
    void setup() {
        sut = new UserDaoImpl(mockUserRepository, mockEncoder);
    }

    @DisplayName("method createUser")
    @Nested
    class CreateUserTest {
        @Test
        void should_return_id_of_saved_user() {
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
    }

    @DisplayName("method findById")
    @Nested
    class FindByIdTest {
        @Test
        public void should_throw_not_found_exception_if_id_null() {
            assertThrows(NotFoundException.class, () -> sut.findById(null));
        }

        @Test
        public void should_call_user_repository_once() throws NotFoundException {
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
        public void should_throw_not_found_exception_if_not_found() {
            when(mockUserRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> sut.findById(47L));
        }

        @Test
        public void should_return_user_with_right_id() throws NotFoundException {
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

    @DisplayName("method findAllById")
    @Nested
    class FindAllByIdTest {
        @Test
        void when_findAllById_of_userRepository_return_less_user_than_given_set_user_id_should_throw_NotFoundException() {
            var setUserId = Set.of(30L, 31L);

            var userRole = new RoleEntity().setId(1L).setName(RoleName.ROLE_USER);
            var foundUser = new UserEntity()
                    .setId(30L)
                    .setEmail("user30@gmail.com")
                    .setUsername("user30")
                    .setRoles(Set.of(userRole))
                    .setVulgar(false);
            when(mockUserRepository.findAllById(setUserId)).thenReturn(List.of(foundUser));

            assertThatThrownBy(() -> sut.findAllById(setUserId))
                    .isExactlyInstanceOf(NotFoundException.class)
                    .hasMessage("Users with ids [31] not found");
        }

        @Test
        void when_findAllById_of_userRepository_return_same_size_users_than_given_set_user_id_should_return_set_domain_users() {
            var setUserId = Set.of(30L, 31L);

            var userRole = new RoleEntity().setId(1L).setName(RoleName.ROLE_USER);
            var foundUser30 = new UserEntity()
                    .setId(30L)
                    .setEmail("user30@gmail.com")
                    .setUsername("user30")
                    .setRoles(Set.of(userRole))
                    .setVulgar(false);
            var foundUser31 = new UserEntity()
                    .setId(31L)
                    .setEmail("user31@gmail.com")
                    .setUsername("user31")
                    .setRoles(Set.of(userRole))
                    .setVulgar(false);
            when(mockUserRepository.findAllById(setUserId)).thenReturn(List.of(foundUser30, foundUser31));

            var result = sut.findAllById(setUserId);

            var expectedUser30 = UserMapper.entityToDomain(foundUser30);
            var expectedUser31 = UserMapper.entityToDomain(foundUser31);
            assertThat(result).isEqualTo(Set.of(expectedUser30, expectedUser31));
        }
    }

    @DisplayName("method saveAll")
    @Nested
    class SaveAllTest {
        @Test
        void should_call_save_all_of_user_repository() {
            var user1 = new User()
                    .setName("user1")
                    .setEmail("user1@gmail.com")
                    .setPassword("user1password")
                    .setRoles(Set.of(new Role().setId(1L).setName(RoleName.ROLE_USER)))
                    .setVulgar(false);
            var user2 = new User()
                    .setName("user2")
                    .setEmail("user2@gmail.com")
                    .setPassword("user2password")
                    .setRoles(Set.of(new Role().setId(1L).setName(RoleName.ROLE_USER)))
                    .setVulgar(false);
            var setUser = Set.of(user1, user2);

            sut.saveAll(setUser);

            var expectedUser1 = UserMapper.domainToEntity(user1);
            var expectedUser2 = UserMapper.domainToEntity(user2);
            var expectedSetUser = Set.of(expectedUser1, expectedUser2);

            verify(mockUserRepository, times(1)).saveAll(expectedSetUser);
        }

        @Test
        void when_userRepository_save_all_users_should_return_saved_users() {
            var user1 = new User()
                    .setName("user1")
                    .setEmail("user1@gmail.com")
                    .setPassword("user1password")
                    .setRoles(Set.of(new Role().setId(1L).setName(RoleName.ROLE_USER)))
                    .setVulgar(false);
            var user2 = new User()
                    .setName("user2")
                    .setEmail("user2@gmail.com")
                    .setPassword("user2password")
                    .setRoles(Set.of(new Role().setId(1L).setName(RoleName.ROLE_USER)))
                    .setVulgar(false);
            var setUser = Set.of(user1, user2);
            var expectedUser1 = UserMapper.domainToEntity(user1);
            var expectedUser2 = UserMapper.domainToEntity(user2);
            var expectedSetUser = Set.of(expectedUser1, expectedUser2);

            var savedUser1 = new UserEntity()
                    .setId(1L)
                    .setUsername("user1")
                    .setEmail("user1@gmail.com")
                    .setPassword("user1password")
                    .setRoles(Set.of(new RoleEntity().setId(1L).setName(RoleName.ROLE_USER)))
                    .setVulgar(false);
            var savedUser2 = new UserEntity()
                    .setId(2L)
                    .setUsername("user2")
                    .setEmail("user2@gmail.com")
                    .setPassword("user2password")
                    .setRoles(Set.of(new RoleEntity().setId(1L).setName(RoleName.ROLE_USER)))
                    .setVulgar(false);
            var listSavedUser = List.of(savedUser1, savedUser2);
            when(mockUserRepository.saveAll(expectedSetUser)).thenReturn(listSavedUser);

            var result = sut.saveAll(setUser);

            var expectedSetSavedUser = listSavedUser.stream()
                    .map(UserMapper::entityToDomain)
                    .collect(Collectors.toSet());
            assertThat(result).isEqualTo(expectedSetSavedUser);
        }
    }

    @DisplayName("method findAll")
    @Nested
    class FindAllTest {
        @Test
        void should_call_find_all_of_user_repository() {
            sut.findAll();

            verify(mockUserRepository, times(1)).findAll();
        }

        @Test
        void when_get_users_should_return_set_domain_user() {
            var listUserEntity = List.of(new UserEntity()
                    .setId(2L)
                    .setUsername("name")
                    .setPassword("password")
                    .setEmail("email@gmail.com")
                    .setVulgar(true)
                    .setRoles(Set.of(new RoleEntity().setId(1L).setName(RoleName.ROLE_USER))));
            when(mockUserRepository.findAll()).thenReturn(listUserEntity);

            var result = sut.findAll();

            var expected = listUserEntity.stream()
                    .map(UserMapper::entityToDomain)
                    .collect(Collectors.toSet());
            assertThat(result).isEqualTo(expected);
        }
    }
}
