package com.gotta_watch_them_all.app.comment.infrastructure.dao;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity.CommentEntity;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao {
  private final CommentRepository commentRepository;

  @Override
  public Long createComment(String content, Long userId, Long workId) {
    var savedCommentEntity = commentRepository.save(
        new CommentEntity()
            .setContent(content)
            .setUserId(userId)
            .setWorkId(workId)
    );
    return savedCommentEntity.getId();
  }
}
