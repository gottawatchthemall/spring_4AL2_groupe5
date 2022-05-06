package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeleteCommentById {
    private final CommentDao commentDao;
    private final UpdateUsersVulgarEventPublisher usersVulgarEventPublisher;

    public void execute(Long commentId) {
        var foundComment = commentDao.findById(commentId);
        if (foundComment == null) {
            var message = String.format(
                    "Comment with id '%d' not found",
                    commentId
            );
            throw new NotFoundException(message);
        }
        commentDao.deleteComment(commentId);

        usersVulgarEventPublisher.publishEvent(Set.of(foundComment.getUserId()));
    }
}
