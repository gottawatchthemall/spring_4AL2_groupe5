package com.gotta_watch_them_all.app.media.core;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Media {
    private Long id;
    private String name;
}
