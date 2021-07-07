package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.adapter;

import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response.WorkByTitleResponse;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response.WorkWithDetailsResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class WorkAdapter {
    public static WorkByTitleResponse domainToTitleResponse(Work work) {
        return new WorkByTitleResponse()
                .setTitle(work.getTitle())
                .setType(work.getMedia().getName())
                .setYear(work.getYear())
                .setImdbId(work.getImdbId())
                .setPoster(work.getPoster());
    }

    public static WorkWithDetailsResponse toDetailResponse(Work work) {
        return new WorkWithDetailsResponse()
                .setId(work.getId())
                .setTitle(work.getTitle())
                .setYear(work.getYear())
                .setReleasedDate(work.getReleasedDate())
                .setDuration(work.getDuration())
                .setGenres(work.getGenres())
                .setDirectors(work.getDirectors())
                .setWriters(work.getWriters())
                .setActors(work.getActors())
                .setPlot(work.getPlot())
                .setCountry(work.getCountry())
                .setAwards(work.getAwards())
                .setMedia(work.getMedia())
                .setScore(work.getScore())
                .setComments(work.getComments())
                .setImdbId(work.getImdbId())
                .setPoster(work.getPoster());
    }

    public static Set<WorkByTitleResponse> domainToTitleResponseSet(Set<Work> works) {
        return works
                .stream()
                .map(WorkAdapter::domainToTitleResponse)
                .collect(Collectors.toSet());
    }

    public static Set<WorkWithDetailsResponse> toDetailResponses(Set<Work> works) {
        return works
                .stream()
                .map(WorkAdapter::toDetailResponse)
                .collect(Collectors.toSet());
    }
}
