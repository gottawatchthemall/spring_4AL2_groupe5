package com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.repository;

import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
  Boolean existsByWorkIdAndUserId(Long workId, Long userId);
  Set<CommentEntity> findByWorkId(Long workId);

  Set<CommentEntity> findAllByUserId(Long userId);
}
