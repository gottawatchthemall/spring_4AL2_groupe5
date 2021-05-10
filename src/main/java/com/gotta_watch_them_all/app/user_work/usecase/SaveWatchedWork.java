package com.gotta_watch_them_all.app.user_work.usecase;

import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveWatchedWork {

    private final UserDao userDao;

    @Qualifier("mySqlDao")
    private final WorkDao workDao;

    public Long execute(Long userId, String imdbId) throws NotFoundException {
        var user = userDao.findById(userId);
        if (user == null) throw new NotFoundException(String.format("User with id %s does not exists", userId));
        //check user id exists
        //check imdbId exists
        //if all true
        //check if work already stored, if not store it
        //then check if userwork doesn"t already exists
        //then store occurrence with userId and workId stored
        return null;
    }
}
