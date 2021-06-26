package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateComment {

  private final CommentDao commentDao;

  public Long execute(String content, Long userId, Long workId) {
    return commentDao.createComment(content, userId, workId);
  }
}
