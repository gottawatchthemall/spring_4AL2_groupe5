package com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SaveBannedWordRequest {
    private String word;
}
