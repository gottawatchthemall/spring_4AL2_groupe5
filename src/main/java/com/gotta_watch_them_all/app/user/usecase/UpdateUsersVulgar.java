package com.gotta_watch_them_all.app.user.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateUsersVulgar {
    private final UserDao userDao;
    private final CommentDao commentDao;
    private static final int LIMIT_NUMBER_VULGAR_COMMENTS = 3;

    public void execute(Set<Long> setUserId) {
        var foundUsers = userDao.findAllById(setUserId);

        foundUsers.forEach(user -> {
            var userComments = commentDao.findAllByUserId(user.getId());
            var numberVulgarComments = getNumberVulgarComments(userComments);

            user.setVulgar(isNumberVulgarCommentsEqualOrExceedLimit(numberVulgarComments));
        });

        userDao.saveAll(foundUsers);
    }

    private Long getNumberVulgarComments(Set<Comment> userComments) {
        return userComments.stream()
                .filter(Comment::isVulgar)
                .count();
    }

    private boolean isNumberVulgarCommentsEqualOrExceedLimit(long numberVulgarComments) {
        return numberVulgarComments >= LIMIT_NUMBER_VULGAR_COMMENTS;
    }
}
