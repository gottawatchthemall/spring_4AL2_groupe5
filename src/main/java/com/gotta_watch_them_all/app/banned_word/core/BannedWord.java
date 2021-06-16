package com.gotta_watch_them_all.app.banned_word.core;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BannedWord {
    private Long id;
    private String word;
}
