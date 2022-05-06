package com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "comment")
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CommentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String content;

  private boolean vulgar = false;

  private Long userId;

  private Long workId;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Zagreb")
  private LocalDateTime publishAt;
}
