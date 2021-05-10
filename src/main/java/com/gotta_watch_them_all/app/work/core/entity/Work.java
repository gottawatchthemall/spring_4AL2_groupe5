package com.gotta_watch_them_all.app.work.core.entity;

import com.gotta_watch_them_all.app.core.entity.Media;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class Work {
    private Long id;
    private String imdbId;
    private String title;
    private String year;
    private LocalDate releasedDate;
    private Integer duration;
    private Set<String> genres;
    private Set<String> directors;
    private Set<String> writers;
    private Set<String> actors;
    private String plot;
    private String country;
    private String awards;
    private String poster;
    private Media media;
    private Integer score;
}
