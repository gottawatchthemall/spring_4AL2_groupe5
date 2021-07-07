package com.gotta_watch_them_all.app.unit.user.usecase;

import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.usecase.FindAllUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        sut.execute(true);

        verify(mockUserDao, times(1)).findAll();
    }
}