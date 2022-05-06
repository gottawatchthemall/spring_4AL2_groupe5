package com.gotta_watch_them_all.app.work.core.entity;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.media.core.Media;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class Work {
    private Long id;
    private String imdbId;
    private String title;
    private String year;
    private String releasedDate;
    private String duration;
    private String genres;
    private String directors;
    private String writers;
    private String actors;
    private String plot;
    private String country;
    private String awards;
    private String poster;
    private Media media;
    private Integer score;
    private Set<Comment> comments;
}
