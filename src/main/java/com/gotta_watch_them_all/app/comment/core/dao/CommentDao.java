package com.gotta_watch_them_all.app.comment.core.dao;

public interface CommentDao {
    Long createComment(String content, Long workId, Long userId);
}
