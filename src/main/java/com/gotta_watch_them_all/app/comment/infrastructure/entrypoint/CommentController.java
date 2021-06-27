package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint;

import com.gotta_watch_them_all.app.comment.usecase.CreateComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

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
      @Valid @RequestBody CreateCommentRequest comment,
      @ApiIgnore @RequestAttribute("userId") String userId
  ) {
    var commentId = createComment.execute(comment.getContent(), Long.parseLong(userId), comment.getWorkId());
    var uid = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(commentId)
        .toUri();
    return created(uid).build();
  }
}
