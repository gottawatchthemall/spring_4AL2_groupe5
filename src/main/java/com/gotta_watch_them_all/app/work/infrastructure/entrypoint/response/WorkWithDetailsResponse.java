package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.media.core.Media;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@JsonDeserialize
@Accessors(chain = true)
public class WorkWithDetailsResponse {
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
