package com.gotta_watch_them_all.app.user_work.core.dao;


import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;

public interface UserWorkDao {
    void save(UserWork userWork);

    UserWork findById(Long userId, Long workId);
}
