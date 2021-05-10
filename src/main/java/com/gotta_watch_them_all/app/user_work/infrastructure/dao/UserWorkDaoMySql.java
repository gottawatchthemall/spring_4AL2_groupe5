package com.gotta_watch_them_all.app.user_work.infrastructure.dao;

import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWorkDaoMySql implements UserWorkDao {

    @Override
    public void save(UserWork userWork) {

    }

    @Override
    public UserWork findById(Long userId, Long workId) {
        return null;
    }
}
