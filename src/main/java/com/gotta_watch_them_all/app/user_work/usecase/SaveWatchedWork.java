package com.gotta_watch_them_all.app.user_work.usecase;

import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
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

    public Long execute(Long userId, String imdbId) throws NotFoundException, IllegalImdbIdGivenException {
        var user = userDao.findById(userId);
        //TODO Voir où vraiment throw les exceptions
        if (user == null) {
            throw new NotFoundException(String.format("User with id %s does not exists", userId));
        }
        var work = workDaoMySql.findByImdbId(imdbId);

        if (work == null) {
            work = workDaoMovieDbApi.findByImdbId(imdbId);
        }
        if (work == null) {
            throw new IllegalImdbIdGivenException(String.format("Wrong imdbid %s", imdbId));
        }

        //check user id exists
        //check imdbId exists
        //if all true
        //check if work already stored, if not store it
        //TODO then check if userwork doesn"t already exists
        //then store occurrence with userId and workId stored
        return null;
    }
}
