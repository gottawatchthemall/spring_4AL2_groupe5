package com.gotta_watch_them_all.app.user_work.usecase;


import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.usecase.FindCommentsByWorkId;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindWorksWatchedByOneUser {

    private final UserDao userDao;
    private final FindCommentsByWorkId findCommentsByWorkId;

    @Qualifier("mySqlDao")
    private final WorkDao workDaoMySql;

    @Qualifier("movieDbApiDao")
    private final WorkDao workDaoMovieDbApi;

    private final UserWorkDao userWorkDao;

    public Set<Work> execute(Long userId) {
        final var user = findUserById(userId);
        return userWorkDao.findWorksByUser(user)
            .stream()
            .map(work -> {
                var comments = findCommentsByWorkId.execute(work.getId());
                return work.setComments(comments);
            }).collect(Collectors.toSet());
    }

    private User findUserById(Long userId) {
        var user = userDao.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("User with id %s does not exists", userId));
        }
        return user;
    }
}
