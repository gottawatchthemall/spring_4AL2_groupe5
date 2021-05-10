package com.gotta_watch_them_all.app.unit.auth.usecase;

import com.gotta_watch_them_all.app.core.dao.RoleDao;
import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.core.entity.Role;
import com.gotta_watch_them_all.app.core.entity.RoleName;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.auth.usecase.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpTest {
    @Mock
    private UserDao mockUserDao;

    @Mock
    private RoleDao mockRoleDao;

    private SignUp sut;

    private final String userName = "user name";
    private final String email = "user@email.fr";
    private final String password = "password";

    @BeforeEach
    void setup() {
        sut = new SignUp(mockUserDao, mockRoleDao);
    }

    @Test
    void should_check_if_username_already_exists() throws AlreadyCreatedException, NotFoundException {
        sut.execute(userName, email, password, null);
        verify(mockUserDao, times(1)).existsByUsername(userName);
    }

    @Test
    void when_username_already_exists_should_throw_exception() {
        when(mockUserDao.existsByUsername(userName)).thenReturn(true);

        assertThatThrownBy(() -> sut.execute(userName, email, password, null)).isExactlyInstanceOf(AlreadyCreatedException.class)
                .hasMessage("User with username '" + userName + "' already exists");
    }

    @Test
    void should_check_if_email_already_exists() throws AlreadyCreatedException, NotFoundException {
        sut.execute(userName, email, password, null);
        verify(mockUserDao, times(1)).existsByEmail(email);
    }

    @Test
    void when_email_already_exists_should_throw_exception() {
        when(mockUserDao.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> sut.execute(userName, email, password, null))
                .isExactlyInstanceOf(AlreadyCreatedException.class)
                .hasMessage("User with email '" + email + "' already exists");
    }

    @Test
    void when_strRoles_contain_user_should_call_roleDao_to_find_userRole() throws AlreadyCreatedException, NotFoundException {
        sut.execute(userName, email, password, Set.of("user"));
        verify(mockRoleDao, times(1)).findByRoleName(RoleName.ROLE_USER);
    }

    @Test
    void when_strRoles_contain_admin_should_call_roleDao_to_find_adminRole() throws AlreadyCreatedException, NotFoundException {
        sut.execute(userName, email, password, Set.of("admin"));
        verify(mockRoleDao, times(1)).findByRoleName(RoleName.ROLE_ADMIN);
    }

    @Test
    void when_strRoles_is_not_empty_but_not_contain_user_or_admin_should_throw_exception() {
        assertThatThrownBy(() -> sut.execute(userName, email, password, Set.of("notRole")))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Role name 'notRole' not found");
    }

    @Test
    void when_strRoles_contain_one_admin_role_should_create_user_with_list_of_one_role() throws NotFoundException, AlreadyCreatedException {
        var adminRole = new Role().setId(1L).setName(RoleName.ROLE_ADMIN);
        var roles = Set.of(adminRole);

        when(mockRoleDao.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);

        sut.execute(userName, email, password, Set.of("admin"));

        verify(mockUserDao, times(1)).createUser(userName, email, password, roles);
    }

    @Test
    void when_strRoles_contain_admin_and_user_roles_should_create_user_with_list_of_admin_and_user_roles() throws NotFoundException, AlreadyCreatedException {
        var adminRole = new Role().setId(1L).setName(RoleName.ROLE_ADMIN);
        var userRole = new Role().setId(2L).setName(RoleName.ROLE_USER);
        var roles = Set.of(adminRole, userRole);

        when(mockRoleDao.findByRoleName(RoleName.ROLE_USER)).thenReturn(userRole);
        when(mockRoleDao.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);

        sut.execute(userName, email, password, Set.of("user", "admin"));

        verify(mockUserDao, times(1)).createUser(userName, email, password, roles);
    }

    @Test
    void when_strRoles_is_null_should_create_user_with_list_of_user_role() throws NotFoundException, AlreadyCreatedException {
        var userRole = new Role().setId(2L).setName(RoleName.ROLE_USER);
        var roles = Set.of(userRole);

        when(mockRoleDao.findByRoleName(RoleName.ROLE_USER)).thenReturn(userRole);

        sut.execute(userName, email, password, null);

        verify(mockUserDao, times(1)).createUser(userName, email, password, roles);
    }
}
