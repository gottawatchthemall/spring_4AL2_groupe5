package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.request.SaveWorkRequest;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response.WorkByTitleResponse;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.adapter.WorkAdapter;
import com.gotta_watch_them_all.app.work.usecase.FindWorkByTitleFromApi;
import com.gotta_watch_them_all.app.work.usecase.SaveWatchedWork;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/works")
public class WorkController {

    private final FindWorkByTitleFromApi findWorkByTitleFromApi;
    private final SaveWatchedWork saveWatchedWork;

    @GetMapping("/moviedb/{title}")
    public ResponseEntity<Set<WorkByTitleResponse>> findByTitle(
            @PathVariable("title") String title
    ) throws NotFoundException, IllegalTitleGivenException {
        var works = findWorkByTitleFromApi.execute(title);
        return ok(WorkAdapter.domainToTitleResponseSet(works));
    }

    @PostMapping
    public ResponseEntity<URI> saveWatchedWork(
            @Valid @RequestBody SaveWorkRequest request
    ) {
        //var newWork = saveWatchedWork.execute(request.getId());
        return null;
    }


}
