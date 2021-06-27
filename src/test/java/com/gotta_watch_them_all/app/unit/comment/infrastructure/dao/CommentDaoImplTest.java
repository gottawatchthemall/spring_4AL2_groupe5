package com.gotta_watch_them_all.app.unit.comment.infrastructure.dao;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.infrastructure.dao.CommentDaoMySql;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity.CommentEntity;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.mapper.CommentMapper;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentDaoImplTest {
  private final long userId = 1L;
  private final long workId = 150L;
  private final String content = "a good content";

  private CommentDaoMySql sut;

  @Mock
  private CommentRepository mockCommentRepository;


  @BeforeEach
  void setup() {
    sut = new CommentDaoMySql(mockCommentRepository);
  }

  @Nested
  class CreateCommentTest {
    @Test
    void when_create_comment_repository_is_save_should_return_id_of_save_comment() {
      var expectedId = 10L;
      var commentEntity = new CommentEntity()
          .setContent(content)
          .setUserId(userId)
          .setWorkId(workId);

      var savedCommentEntity = new CommentEntity()
          .setContent(content)
          .setUserId(userId)
          .setWorkId(workId);


      savedCommentEntity.setId(expectedId);
      when(mockCommentRepository.save(commentEntity)).thenReturn(savedCommentEntity);
      var result = sut.createComment(content, userId, workId);
      verify(mockCommentRepository, times(1)).save(commentEntity);

      assertThat(result).isEqualTo(expectedId);
    }
  }

  @Nested
  class SaveCommentTest {
    @Test
    void when_save_comment_repository_should_return_save_comment() {
      var expectedId = 10L;
      var comment = new Comment()
          .setContent(content)
          .setUserId(userId)
          .setWorkId(workId);
      var commentEntity = CommentMapper.domainToEntity(comment);
      var savedCommentEntity = new CommentEntity()
          .setId(expectedId)
          .setContent(content)
          .setUserId(userId)
          .setWorkId(workId);


      when(mockCommentRepository.save(commentEntity)).thenReturn(savedCommentEntity);

      var result = sut.save(comment);
      var expectedComment = CommentMapper.entityToDomain(savedCommentEntity);

      verify(mockCommentRepository, times(1)).save(commentEntity);
      assertThat(result).isEqualTo(expectedComment);
    }

    @Test
    void when_saved_comment_with_id_return_the_same_comment() {
      var expectedId = 10L;
      var comment = new Comment()
          .setId(expectedId)
          .setContent(content)
          .setUserId(userId)
          .setWorkId(workId);

      var commentEntity = CommentMapper.domainToEntity(comment);
      var savedCommentEntity = new CommentEntity()
          .setId(expectedId)
          .setContent(content)
          .setUserId(userId)
          .setWorkId(workId);

      when(mockCommentRepository.save(commentEntity)).thenReturn(savedCommentEntity);


      var result = sut.save(comment);
      assertThat(result).isEqualTo(comment);
    }
  }
}