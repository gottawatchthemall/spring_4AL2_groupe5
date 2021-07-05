package com.gotta_watch_them_all.app.user_work.usecase;


import com.gotta_watch_them_all.app.comment.usecase.FindCommentsByWorkId;
import com.gotta_watch_them_all.app.user_work.core.dao.UserWorkDao;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FindWatchedWork {

    private final UserWorkDao userWorkDao;
    private final FindCommentsByWorkId findCommentsByWorkId;

    public UserWork execute(Long userId, Long workId) {
        var userWork = userWorkDao.findById(userId, workId);
        var comments = findCommentsByWorkId.execute(workId);
        var finalWork = userWork.getWork().setComments(comments);
        return userWork.setWork(finalWork);
    }
}
