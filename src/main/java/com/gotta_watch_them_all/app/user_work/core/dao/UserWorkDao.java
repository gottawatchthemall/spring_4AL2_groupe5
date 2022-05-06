package com.gotta_watch_them_all.app.user_work.core.dao;


import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.work.core.entity.Work;

import java.util.Set;

public interface UserWorkDao {
    UserWork save(UserWork userWork);

    void delete(UserWork userWork);

    UserWork findById(Long userId, Long workId);

    Set<Work> findWorksByUser(User user);
}
