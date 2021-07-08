package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllComment {
    private final UserDao userDao;
    private final CommentDao commentDao;

    public Set<Comment> execute() {
      return commentDao.findAll()
          .stream()
          .peek(comment -> {
            var user = userDao.findById(comment.getUserId());

            comment.setUsername(user.getName());

            if (comment.isVulgar()) {
              comment.setContent("this message has been moderated");
            }
          }).collect(Collectors.toSet());
    }
}
