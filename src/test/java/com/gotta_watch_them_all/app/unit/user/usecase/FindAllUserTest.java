package com.gotta_watch_them_all.app.unit.user.usecase;

import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user.infrastructure.entrypoint.adapter.UserAdapter;
import com.gotta_watch_them_all.app.user.usecase.FindAllUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static java.util.Optional.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserTest {
    private FindAllUser sut;

    @Mock
    private UserDao mockUserDao;

    @BeforeEach
    void setup() {
        sut = new FindAllUser(mockUserDao);
    }

    @Test
    void should_call_userDao_find_all() {
        sut.execute(of(true));

        verify(mockUserDao, times(1)).findAll();
    }

    @Test
    void when_find_all_users_should_return_set_vulgar_users() {
        var normalUser = new User()
                .setId(3L)
                .setVulgar(false)
                .setName("user")
                .setEmail("user@gmail.gom")
                .setPassword("password")
                .setRoles(Set.of(new Role().setId(3L).setName(RoleName.ROLE_USER)));
        var vulgarUser = new User()
                .setId(3L)
                .setVulgar(true)
                .setName("vulgar")
                .setEmail("vulgar@gmail.gom")
                .setPassword("vulgarpassword")
                .setRoles(Set.of(new Role().setId(3L).setName(RoleName.ROLE_USER)));
        var setUser = Set.of(normalUser, vulgarUser);

        when(mockUserDao.findAll()).thenReturn(setUser);

        var result = sut.execute(of(true));

        var expectedDtoVulgarUser = UserAdapter.domainToDto(vulgarUser);
        var expected = Set.of(expectedDtoVulgarUser);

        assertThat(result).isEqualTo(expected);
    }
}