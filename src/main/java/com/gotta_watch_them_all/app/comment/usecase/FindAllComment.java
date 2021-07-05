package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllComment {
    private final CommentDao commentDao;

    public Set<Comment> execute() {
      return commentDao.findAll()
          .stream()
          .peek(comment -> {
            if (comment.isVulgar()) {
              comment.setContent("this message has been moderated");
            }
          }).collect(Collectors.toSet());
    }
}
