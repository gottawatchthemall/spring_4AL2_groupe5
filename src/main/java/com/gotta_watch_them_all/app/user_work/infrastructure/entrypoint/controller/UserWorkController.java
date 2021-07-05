package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.comment.usecase.FindCommentsByWorkId;
import com.gotta_watch_them_all.app.user_work.usecase.FindWatchedWork;
import com.gotta_watch_them_all.app.user_work.usecase.FindWorksWatchedByOneUser;
import com.gotta_watch_them_all.app.user_work.usecase.RemoveWatchedWork;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/watched")
public class UserWorkController {

    private final SaveWatchedWork saveWatchedWork;
    private final FindWatchedWork findWatchedWork;
    private final RemoveWatchedWork removeWatchedWork;
    private final FindCommentsByWorkId findCommentsByWorkId;
    private final FindWorksWatchedByOneUser findWorksWatchedByOneUser;

    @PutMapping("/works/{workId}")
    public ResponseEntity<?> saveWatchedWork(
            @PathVariable("workId")
            @NotBlank(message = "id has to be") String workId,
            @ApiIgnore @RequestAttribute("userId") String userId
    ) {
        final var newUserWork = saveWatchedWork.execute(Long.valueOf(userId), workId);

        final var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/watched/works/{workId}")
                .buildAndExpand(
                        Map.of("workId", newUserWork.getWork().getId()))
                .toUri();

        var comments = findCommentsByWorkId.execute(newUserWork.getWork().getId());
        var newUserWorkWithComments = newUserWork.getWork().setComments(comments);

        return ResponseEntity.created(uri).body(newUserWorkWithComments);
    }

    @DeleteMapping("/works/{workId}")
    public ResponseEntity<?> removeWatchedWork(
            @ApiIgnore @RequestAttribute("userId") String userId,
            @PathVariable("workId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long workId
    ) {
        removeWatchedWork.execute(Long.valueOf(userId), workId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/works")
    public ResponseEntity<?> getWatchedWorkByCurrentUser(
            @ApiIgnore @RequestAttribute("userId") String userId
    ) {
        final var works = findWorksWatchedByOneUser.execute(Long.valueOf(userId));
        return ResponseEntity.ok(works);
    }

    @GetMapping("/works/{workId}")
    public ResponseEntity<?> getWatchedWorkById(
            @ApiIgnore @RequestAttribute("userId") String userId,
            @PathVariable("workId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long workId
    ) {
        final var work = findWatchedWork.execute(Long.valueOf(userId), workId);
        return ResponseEntity.ok(work);
    }

    @GetMapping("/users/{userId}/works")
    public ResponseEntity<?> getWatchedWorkByUser(
            @PathVariable("userId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long userId
    ) {
        final var works = findWorksWatchedByOneUser.execute(userId);
        return ResponseEntity.ok(works);
    }
}
