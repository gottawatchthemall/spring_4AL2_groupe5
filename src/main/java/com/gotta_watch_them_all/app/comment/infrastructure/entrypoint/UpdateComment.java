package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class UpdateComment {
  @NotBlank
  private String content;
}
