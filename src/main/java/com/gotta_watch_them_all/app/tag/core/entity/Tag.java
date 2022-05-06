package com.gotta_watch_them_all.app.tag.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Tag {
    private Long id;
    private String name;
}
