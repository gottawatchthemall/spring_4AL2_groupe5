package com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity.CommentEntity;

public class CommentMapper {
  public static Comment entityToDomain(CommentEntity entity) {
    return new Comment()
        .setId(entity.getId())
        .setContent(entity.getContent())
        .setVulgar(entity.isVulgar())
        .setWorkId(entity.getWorkId())
        .setUserId(entity.getUserId());
  }

  public static CommentEntity domainToEntity(Comment comment) {
    return new CommentEntity()
        .setId(comment.getId())
        .setContent(comment.getContent())
        .setVulgar(comment.isVulgar())
        .setWorkId(comment.getWorkId())
        .setUserId(comment.getUserId());
  }
}
