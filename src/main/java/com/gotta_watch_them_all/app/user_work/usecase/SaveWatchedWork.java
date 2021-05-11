package com.gotta_watch_them_all.app.user_work.usecase;

import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.TooManySearchArgumentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveWatchedWork {

    private final UserDao userDao;

    @Qualifier("mySqlDao")
    private final WorkDao workDaoMySql;

    @Qualifier("movieDbApiDao")
    private final WorkDao workDaoMovieDbApi;

    private final UserWorkDao userWorkDao;

    public UserWork execute(Long userId, String imdbId) throws NotFoundException, IllegalImdbIdGivenException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        var user = findUserById(userId);
        var work = findWorkByImdbId(imdbId);

        checkIfUserWorkAlreadyExists(userId, work.getId());

        UserWork newUserWork = new UserWork().setWork(work).setUser(user);
        return userWorkDao.save(newUserWork);
    }

    private void checkIfUserWorkAlreadyExists(Long userId, Long workId) throws AlreadyCreatedException {
        UserWork userWorkAlreadyExists = null;
        try {
            userWorkAlreadyExists = userWorkDao.findById(userId, workId);
        } catch (NotFoundException ignored) {
        }
        if (userWorkAlreadyExists != null) {
            throw new AlreadyCreatedException(String.format("Work %s already saved for user %s",
                    workId, userId));
        }
    }

    private User findUserById(Long userId) throws NotFoundException {
        var user = userDao.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("User with id %s does not exists", userId));
        }
        return user;
    }

    private Work findWorkByImdbId(String imdbId) throws IllegalImdbIdGivenException, NotFoundException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Work work = null;
        try {
            work = workDaoMySql.findByImdbId(imdbId);
        } catch (IllegalImdbIdGivenException | NotFoundException ignored) {
        }

        if (work == null) {
            work = workDaoMovieDbApi.findByImdbId(imdbId);
            if (work == null) {
                throw new IllegalImdbIdGivenException(String.format("Wrong imdbid %s", imdbId));
            } else {
                work = workDaoMySql.save(work);
            }
        }
        return work;
    }
}
