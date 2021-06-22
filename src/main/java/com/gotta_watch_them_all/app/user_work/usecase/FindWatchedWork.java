package com.gotta_watch_them_all.app.user_work.usecase;


import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FindWatchedWork {

    private final UserWorkDao userWorkDao;

    public UserWork execute(Long userId, Long workId) {
        return userWorkDao.findById(userId, workId);
    }
}
