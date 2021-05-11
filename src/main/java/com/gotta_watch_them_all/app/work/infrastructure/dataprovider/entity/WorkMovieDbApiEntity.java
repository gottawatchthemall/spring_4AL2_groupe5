package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gotta_watch_them_all.app.core.entity.Media;
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

    @JsonProperty("Released")
    private String releasedDate;

    @JsonProperty("Runtime")
    private String duration;

    @JsonProperty("Genre")
    private String genres;

    @JsonProperty("Director")
    private String directors;

    @JsonProperty("Writer")
    private String writers;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("Metascore")
    private String score;
}
