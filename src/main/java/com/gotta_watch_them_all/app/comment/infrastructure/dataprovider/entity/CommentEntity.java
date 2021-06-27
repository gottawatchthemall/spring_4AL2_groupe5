package com.gotta_watch_them_all.app.comment.infrastructure.dataprovider.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

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
}
