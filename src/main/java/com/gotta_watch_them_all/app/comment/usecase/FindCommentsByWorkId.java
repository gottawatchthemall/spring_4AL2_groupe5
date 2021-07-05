package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindCommentsByWorkId {

  private final CommentDao commentDao;
  private final WorkRepository workRepository;

  public Set<Comment> execute(Long workId) throws NotFoundException {
    if (!workRepository.existsById(workId)) {
      var message = String.format(
          "workId with id '%d' not found", workId
      );
      throw new NotFoundException(message);
    }

    return commentDao.findByWorkId(workId)
        .stream()
        .peek(comment -> {
          if (comment.isVulgar()) {
            comment.setContent("this message has been moderated");
          }
        }).collect(Collectors.toSet());
  }
}
