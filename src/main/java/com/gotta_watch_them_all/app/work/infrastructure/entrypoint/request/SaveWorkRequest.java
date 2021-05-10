package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveWorkRequest {
    @NotBlank(message = "Work should have an id")
    private String id;
}
