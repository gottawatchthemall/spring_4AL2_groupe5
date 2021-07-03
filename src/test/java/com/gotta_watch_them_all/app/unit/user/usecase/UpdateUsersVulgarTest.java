package com.gotta_watch_them_all.app.unit.user.usecase;

import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.usecase.UpdateUsersVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateUsersVulgarTest {
    private UpdateUsersVulgar sut;

    @Mock
    private UserDao mockUserDao;

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
}