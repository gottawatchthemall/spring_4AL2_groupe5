package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@JsonDeserialize
@Accessors(chain = true)
public class WorkResponse {
    private String id;
    private String title;
    private String year;
    private String type;
    private String poster;
}
