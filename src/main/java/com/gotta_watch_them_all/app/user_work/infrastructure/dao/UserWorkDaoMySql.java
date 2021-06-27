package com.gotta_watch_them_all.app.user_work.infrastructure.dao;

import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.entity.UserWorkEntity;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.mapper.UserWorkMapper;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.repository.UserWorkRepository;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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
    public void delete(UserWork userWork) {
        userWorkRepository.delete(mapper.toEntity(userWork));
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

    @Override
    public Set<Work> findWorksByUser(User user) {
        final var workIds = userWorkRepository.findAllByUserId(user.getId())
                .stream()
                .map(UserWorkEntity::getWorkId)
                .collect(Collectors.toSet());
        return workDao.findAllByIds(workIds);
    }

    private void checkIfUserWorkExists(Long userId, Long workId) {
        var userWorkEntity = userWorkRepository.findByUserIdAndWorkId(userId, workId);
        if (userWorkEntity == null) {
            throw new NotFoundException(String.format("UserWork with user id %s and work id %s does not exists",
                    userId, workId));
        }
    }
}
