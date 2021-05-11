package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.controller;


import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.request.SaveWorkRequest;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;
import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.TooManySearchArgumentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
        try {
            var newUserWork = saveWatchedWork.execute(request.getUserId(), request.getImdbId());
            var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/user/{userId}/work/{workId}")
                    .buildAndExpand(
                            Map.of("userId", newUserWork.getUser().getId(),
                                    "workId", newUserWork.getWork().getId()))
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (NotFoundException | TooManySearchArgumentsException
                | IllegalImdbIdGivenException | AnySearchValueFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (AlreadyCreatedException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }


}
