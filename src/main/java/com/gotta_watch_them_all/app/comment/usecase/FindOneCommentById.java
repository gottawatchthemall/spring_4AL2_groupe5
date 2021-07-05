package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.repository.CommentRepository;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindOneCommentById {
    private final UserDao userDao;
    private final CommentDao commentDao;
    private final CommentRepository commentRepository;

    public Comment execute(Long commentId) throws NotFoundException {
      if (!commentRepository.existsById(commentId)) {
        var message = String.format(
            "Comment with id '%d' not found", commentId
        );
        throw new NotFoundException(message);
      }

      var comment = commentDao.findById(commentId);
      var user = userDao.findById(comment.getUserId());

      comment.setUsername(user.getName());

      if(comment.isVulgar()) {
        comment.setContent("this message has been moderated");
      }

      return comment;
    }
}
