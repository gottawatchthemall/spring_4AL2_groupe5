package com.gotta_watch_them_all.app.comment.infrastructure.security;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCreator {
  private final CommentDao commentDao;
  public boolean check(Long commentId, Long userId) {
    return this.commentDao.findById(commentId).getUserId().equals(userId);
  }
}
