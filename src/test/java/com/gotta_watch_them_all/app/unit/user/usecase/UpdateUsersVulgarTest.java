package com.gotta_watch_them_all.app.unit.user.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user.usecase.UpdateUsersVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUsersVulgarTest {
    private UpdateUsersVulgar sut;

    @Mock
    private UserDao mockUserDao;

    @Mock
    private CommentDao mockCommentDao;

    @BeforeEach
    void setup() {
        sut = new UpdateUsersVulgar(mockUserDao);
    }

    @Test
    void should_find_all_users_by_ids() {
        var setUserId = Set.of(14L, 15L);

        sut.execute(setUserId);

        verify(mockUserDao, times(1)).findAllById(setUserId);
    }

    @Test
    void when_findAllById_return_set_users_should_call_findAllByUserId_of_comment_dao() {
        var setUserId = Set.of(14L, 15L);
        var user14 = new User().setId(14L)
                .setEmail("user14@gmail.com")
                .setName("user14")
                .setPassword("userpassword")
                .setRoles(Set.of(new Role().setId(1L).setName(RoleName.ROLE_USER)))
                .setVulgar(false);
        var user15 = new User().setId(15L)
                .setEmail("user15@gmail.com")
                .setName("user15")
                .setPassword("userpassword")
                .setRoles(Set.of(new Role().setId(1L).setName(RoleName.ROLE_USER)))
                .setVulgar(false);
        when(mockUserDao.findAllById(setUserId)).thenReturn(Set.of(user14, user15));

        sut.execute(setUserId);

        verify(mockCommentDao, times(1)).findAllByUserId(14L);
        verify(mockCommentDao, times(1)).findAllByUserId(15L);
    }
}