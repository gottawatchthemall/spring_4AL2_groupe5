package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCommentById {
    private final CommentDao commentDao;

    public void execute(Long commentId) {
        commentDao.deleteComment(commentId);
    }
}