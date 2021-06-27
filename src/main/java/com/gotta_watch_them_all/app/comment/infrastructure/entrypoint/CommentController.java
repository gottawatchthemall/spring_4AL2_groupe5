package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint;

import com.gotta_watch_them_all.app.comment.usecase.CreateComment;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
@Validated
public class CommentController {

  private final CreateComment createComment;

  @PostMapping
  public ResponseEntity<Long> createComment(
      @Valid @RequestBody CreateCommentRequest comment
  ) {
    var commentId = createComment.execute(comment.getContent(), comment.getUserId(), comment.getWorkId());
    var uid = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(commentId)
        .toUri();
    return created(uid).build();
  }
}
