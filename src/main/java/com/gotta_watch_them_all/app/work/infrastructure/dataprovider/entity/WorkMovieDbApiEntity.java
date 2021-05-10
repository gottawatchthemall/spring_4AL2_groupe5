package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WorkMovieDbApiEntity {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("imdbID")
    private String imdbID;
}
