package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class SaveWorkRequest {
    @NotNull(message = "Work id should not be null")
    @Positive(message = "Work id should be positive")
    private Long id;

    @NotBlank(message = "Work should have an imdbId")
    private String imdbId;
}
