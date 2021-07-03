package com.gotta_watch_them_all.app.user.usecase;

import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateUsersVulgar {
    private final UserDao userDao;

    public void execute(Set<Long> setUserId) {
        //userDao.findAllById(setUserId);
    }
}
