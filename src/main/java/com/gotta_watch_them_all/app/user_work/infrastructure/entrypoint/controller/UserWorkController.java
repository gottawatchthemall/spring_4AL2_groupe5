package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.user_work.usecase.RemoveWatchedWork;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserWorkController {

    private final SaveWatchedWork saveWatchedWork;
    private final RemoveWatchedWork removeWatchedWork;

    @PostMapping("/user/{userId}/work/{workId}")
    public ResponseEntity<?> saveWatchedWork(
            @PathVariable("userId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long userId,
            @PathVariable("workId")
            @NotBlank(message = "id has to be") String workId
    ) {
        var newUserWork = saveWatchedWork.execute(userId, workId);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/user/{userId}/work/{workId}")
                .buildAndExpand(
                        Map.of("userId", newUserWork.getUser().getId(),
                                "workId", newUserWork.getWork().getId()))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/user/{userId}/work/{workId}")
    public ResponseEntity<?> removeWatchedWork(
            @PathVariable("userId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long userId,
            @PathVariable("workId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long workId
    ) {
        removeWatchedWork.execute(userId, workId);
        return ResponseEntity.noContent().build();
    }


}
