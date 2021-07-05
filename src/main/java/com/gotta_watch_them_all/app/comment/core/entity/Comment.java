package com.gotta_watch_them_all.app.comment.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Comment {
  private Long id;
  private Long workId;
  private Long userId;
  private String content;
  private boolean vulgar;
  private String username = "unknown";

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime publishAt;
}