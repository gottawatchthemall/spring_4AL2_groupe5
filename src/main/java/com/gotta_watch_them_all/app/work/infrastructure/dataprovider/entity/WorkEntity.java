package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "work")
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column
    private String title;

    @Column
    private String year;

    @Column
    private LocalDate releasedDate;

    @Column
    private Integer duration;

    @Column
    private String genres;

    @Column
    private String directors;

    @Column(columnDefinition = "TEXT")
    private String writers;

    @Column
    private String actors;

    @Column(columnDefinition = "TEXT")
    private String plot;

    @Column
    private String country;

    @Column
    private String awards;

    @Column(columnDefinition = "TEXT")
    private String poster;

    @Column(name = "media_id")
    private Long mediaId;

    @Column
    private Integer score;
}
