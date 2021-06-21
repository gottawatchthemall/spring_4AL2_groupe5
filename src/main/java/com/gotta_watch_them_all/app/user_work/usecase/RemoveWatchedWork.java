package com.gotta_watch_them_all.app.user_work.usecase;


import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveWatchedWork {

    private final UserWorkDao userWorkDao;

    public void execute(Long userId, Long workId) {
        final var userWork = findUserWork(userId, workId);
        userWorkDao.delete(userWork);
    }

    private UserWork findUserWork(Long userId, Long workId) {
        final var userWork = userWorkDao.findById(userId, workId);
        if (userWork == null) {
            throw new NotFoundException(String.format("User %s has not work %s",
                    userId, workId));
        }
        return userWork;
    }
}
