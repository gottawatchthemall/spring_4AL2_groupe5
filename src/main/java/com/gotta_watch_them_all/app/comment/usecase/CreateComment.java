package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateComment {

  private final CommentDao commentDao;
  private final UpdateCommentVulgarByIdEventPublisher updateCommentVulgarPublisher;

  public Long execute(String content, Long userId, Long workId) {
    var savedCommentId = commentDao.createComment(content, userId, workId);

    updateCommentVulgarPublisher.publishEvent(savedCommentId);

    return savedCommentId;
  }
}
