package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEventPublisher;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateCommentById {
    private final Clock clock;
    private final CommentDao commentDao;
    private final UpdateCommentVulgarByIdEventPublisher updateCommentVulgarPublisher;

    public Comment execute(Long commentId, String content) throws NotFoundException {
        if (!commentDao.existsById(commentId)) {
            var message = String.format(
                    "Comment with id '%d' not found", commentId
            );
            throw new NotFoundException(message);
        }

        var newComment = commentDao.findById(commentId).setContent(content);

        newComment.setPublishAt(LocalDateTime.now(clock));

        var savedComment = commentDao.save(newComment);
        updateCommentVulgarPublisher.publishEvent(savedComment.getId());
        return savedComment;
    }
}
