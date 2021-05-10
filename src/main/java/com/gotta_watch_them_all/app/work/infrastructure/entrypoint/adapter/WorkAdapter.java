package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.adapter;

import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response.WorkByTitleResponse;

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

    public static Set<WorkByTitleResponse> domainToTitleResponseSet(Set<Work> work) {
        return work
                .stream()
                .map(WorkAdapter::domainToTitleResponse)
                .collect(Collectors.toSet());
    }
}
