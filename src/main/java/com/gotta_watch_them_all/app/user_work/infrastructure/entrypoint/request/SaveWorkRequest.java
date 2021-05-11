package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class SaveWorkRequest {
    @NotBlank(message = "Work should have an imdbId")
    private String imdbId;

    @NotNull(message = "User id should not be null")
    @Positive(message = "User id should be positive")
    private Long userId;
}
