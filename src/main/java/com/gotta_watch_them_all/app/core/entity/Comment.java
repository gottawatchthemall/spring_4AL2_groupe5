package com.gotta_watch_them_all.app.core.entity;

import com.gotta_watch_them_all.app.work.core.entity.Work;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Comment {
    private Long id;
    private String content;
    private boolean vulgar;
    private User user;
    private Work work;
}
