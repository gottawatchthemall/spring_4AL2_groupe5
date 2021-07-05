package com.gotta_watch_them_all.app.comment.infrastructure.dao;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity.CommentEntity;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.mapper.CommentMapper;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentDaoMySql implements CommentDao {
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

  @Override
  public Set<Comment> findAll() {
    var listComment = commentRepository.findAll();
    return listComment.stream()
            .map(CommentMapper::entityToDomain)
            .collect(Collectors.toSet());
  }

  @Override
  public Set<Comment> findByWorkId(Long workId) {
    return commentRepository.findByWorkId(workId)
        .stream()
        .map(CommentMapper::entityToDomain)
        .collect(Collectors.toSet());
  }

  @Override
  public Comment findById(Long commentId) {
    return commentRepository.findById(commentId)
        .map(CommentMapper::entityToDomain)
        .orElse(null);
  }

  @Override
  public void deleteComment(Long commentId) {
    commentRepository.deleteById(commentId);
  }

  @Override
  public Comment save(Comment comment) {
    var commentEntity = CommentMapper.domainToEntity(comment);
    var commentSaved = commentRepository.save(commentEntity);

    return CommentMapper.entityToDomain(commentSaved);
  }

  @Override
  public void saveAll(Set<Comment> setComment) {
    var setCommentEntity = setComment.stream()
            .map(CommentMapper::domainToEntity)
            .collect(Collectors.toSet());
    commentRepository.saveAll(setCommentEntity);
  }

  @Override
  public Set<Comment> findAllByUserId(Long userId) {
    var foundSetComment = commentRepository.findAllByUserId(userId);
    return foundSetComment.stream()
            .map(CommentMapper::entityToDomain)
            .collect(Collectors.toSet());
  }

}
