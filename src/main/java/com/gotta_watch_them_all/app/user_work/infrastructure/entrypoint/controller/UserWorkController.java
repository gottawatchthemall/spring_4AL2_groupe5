package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.controller;



import com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.request.SaveWorkRequest;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-works")
public class UserWorkController {

    private final SaveWatchedWork saveWatchedWork;

    @PostMapping
    public ResponseEntity<?> saveWatchedWork(
            @Valid @RequestBody SaveWorkRequest request
    ) {
        var newUserWork = saveWatchedWork.execute(request.getUserId(), request.getImdbId());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/user/{userId}/work/{workId}")
                .buildAndExpand(
                        Map.of("userId", newUserWork.getUser().getId(),
                                "workId", newUserWork.getWork().getId()))
                .toUri();
        return ResponseEntity.created(uri).build();

    }


}
