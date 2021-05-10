package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.adapter;

import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response.WorkResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class WorkAdapter {
    public static WorkResponse domainToResponse(Work work) {
        return new WorkResponse()
                .setId(work.getId())
                .setTitle(work.getTitle())
                .setType(work.getMedia().getName())
                .setYear(work.getYear())
                .setPoster(work.getPoster());
    }

    public static Set<WorkResponse> domainToResponseSet(Set<Work> work) {
        return work
                .stream()
                .map(WorkAdapter::domainToResponse)
                .collect(Collectors.toSet());
    }
}
