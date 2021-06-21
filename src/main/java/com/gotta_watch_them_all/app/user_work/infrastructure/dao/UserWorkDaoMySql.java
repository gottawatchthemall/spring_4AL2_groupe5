package com.gotta_watch_them_all.app.user_work.infrastructure.dao;

import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.mapper.UserWorkMapper;
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
    private final UserWorkMapper mapper;

    @Override
    public UserWork save(UserWork userWork) {
        var newEntity = userWorkRepository
                .save(mapper.toEntity(userWork));
        return mapper
                .toDomain(newEntity);
    }

    @Override
    public UserWork findById(Long userId, Long workId) {
        checkIfUserWorkExists(userId, workId);
        var user = userDao.findById(userId);
        var work = workDao.findById(workId);

        return new UserWork()
                .setUser(user)
                .setWork(work);
    }

    private void checkIfUserWorkExists(Long userId, Long workId) {
        var userWorkEntity = userWorkRepository.findByUserIdAndWorkId(userId, workId);
        if (userWorkEntity == null) {
            throw new NotFoundException(String.format("UserWork with user id %s and work id %s does not exists",
                    userId, workId));
        }
    }
}
