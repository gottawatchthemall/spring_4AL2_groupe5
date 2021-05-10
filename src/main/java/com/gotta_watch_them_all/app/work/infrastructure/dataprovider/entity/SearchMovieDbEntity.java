package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SearchMovieDbEntity {

    @JsonProperty("Search")
    private List<WorkMovieDbApiEntity> workMovieDbApiEntities;
}
