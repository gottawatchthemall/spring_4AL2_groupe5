package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateCommentRequest {
  @NotBlank(message = "content has to be not blank")
  private String content;

  @NotNull(message = "userId has to be not blank")
  private Long userId;

  @NotNull(message = "workId has to be not blank")
  private Long workId;
}
