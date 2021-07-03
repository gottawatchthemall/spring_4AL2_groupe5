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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
  class FindAllTest {
    @Test
    void should_call_findAll_of_repository() {
      sut.findAll();

      verify(mockCommentRepository, times(1)).findAll();
    }

    @Test
    void when_get_list_comment_should_return_set_comment() {
      var commentEntity1 = new CommentEntity()
              .setId(1L)
              .setContent("content 1")
              .setUserId(userId)
              .setVulgar(false)
              .setWorkId(workId);
      var commentEntity2 = new CommentEntity()
              .setId(2L)
              .setContent("content 2")
              .setUserId(3L)
              .setVulgar(true)
              .setWorkId(5L);
      var listComment = List.of(commentEntity1, commentEntity2);
      when(mockCommentRepository.findAll()).thenReturn(listComment);

      var result = sut.findAll();

      var comment1 = CommentMapper.entityToDomain(commentEntity1);
      var comment2 = CommentMapper.entityToDomain(commentEntity2);
      var expectedSetComment = Set.of(comment1, comment2);

      assertThat(result).isEqualTo(expectedSetComment);
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

  @Nested
  class UpdateCommentAllTest {
    @Test
    void should_call_repository_to_save_all_comment() {
      var commentEntity1 = new Comment()
              .setId(1L)
              .setContent("content 1")
              .setUserId(userId)
              .setVulgar(false)
              .setWorkId(workId);
      var commentEntity2 = new Comment()
              .setId(2L)
              .setContent("content 2")
              .setUserId(3L)
              .setVulgar(true)
              .setWorkId(5L);
      var setComment = Set.of(commentEntity1, commentEntity2);

      sut.saveAll(setComment);

      var expectedSetComment = setComment.stream().map(CommentMapper::domainToEntity)
              .collect(Collectors.toSet());
      verify(mockCommentRepository, times(1)).saveAll(expectedSetComment);
    }
  }

  @Nested
  class findAllByUserIdTest {

    private final long userId = 61L;

    @Test
    void should_call_findAllByUserId_of_comment_repository() {
      sut.findAllByUserId(userId);

      verify(mockCommentRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void when_repository_return_list_comments_by_userId_should_return_set_comment() {
      long workId = 32L;
      var comment34 = new CommentEntity()
              .setId(34L)
              .setContent("comment34")
              .setUserId(userId)
              .setWorkId(workId);
      var comment35 = new CommentEntity()
              .setId(35L)
              .setContent("comment35")
              .setUserId(userId)
              .setWorkId(workId);
      var foundSetComment = Set.of(comment34, comment35);
      when(mockCommentRepository.findAllByUserId(userId)).thenReturn(foundSetComment);

      var result = sut.findAllByUserId(userId);

      var expectedComment34 = CommentMapper.entityToDomain(comment34);
      var expectedComment35 = CommentMapper.entityToDomain(comment35);
      var expectedSetComment = Set.of(expectedComment34, expectedComment35);

      assertThat(result).isEqualTo(expectedSetComment);
    }
  }
}