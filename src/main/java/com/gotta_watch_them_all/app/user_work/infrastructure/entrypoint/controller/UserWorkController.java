package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.controller;


import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.request.SaveWorkRequest;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/works")
public class UserWorkController {

    private final SaveWatchedWork saveWatchedWork;

    @PostMapping
    public ResponseEntity<URI> saveWatchedWork(
            @Valid @RequestBody SaveWorkRequest request
    ) throws NotFoundException {
        saveWatchedWork.execute(request.getUserId(), request.getImdbId());
        return null;
    }


}
