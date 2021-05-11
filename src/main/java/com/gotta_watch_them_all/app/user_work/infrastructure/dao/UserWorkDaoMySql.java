package com.gotta_watch_them_all.app.user_work.infrastructure.dao;

import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.repository.UserWorkRepository;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWorkDaoMySql implements UserWorkDao {

    private final UserWorkRepository userWorkRepository;
    private final UserDao userDao;
    @Qualifier("mySqlDao")
    private final WorkDao workDao;

    @Override
    public void save(UserWork userWork) {

    }

    @Override
    public UserWork findById(Long userId, Long workId) throws NotFoundException {
        var userEntity = userWorkRepository.findByUserIdAndWorkId(userId, workId);
        if (userEntity == null) {
            throw new NotFoundException(String.format("UserWork with user id %s and work id %s does not exists",
                    userId, workId));
        }
        var user = userDao.findById(userId);
        var work = workDao.findById(workId);

        return new UserWork()
                .setUser(user)
                .setWork(work);
    }
}
