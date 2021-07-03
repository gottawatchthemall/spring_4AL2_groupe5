package com.gotta_watch_them_all.app.comment.core.dao;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;

import java.util.Set;

public interface CommentDao {
    Comment save(Comment comment);

    Comment findById(Long commentId);

    Set<Comment> findAll();

    Set<Comment> findByWorkId(Long workId);

    void deleteComment(Long commentId);

    Long createComment(String content, Long workId, Long userId);

    void saveAll(Set<Comment> setComment);

    Set<Comment> findAllByUserId(Long userId);
}
