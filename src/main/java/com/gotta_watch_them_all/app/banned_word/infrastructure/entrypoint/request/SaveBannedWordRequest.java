package com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class SaveBannedWordRequest {

    @NotBlank
    private String word;
}
