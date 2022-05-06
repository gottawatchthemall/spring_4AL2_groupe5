package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.adapter.UserWorkAdapter;
import com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.response.UserWorkResponse;
import com.gotta_watch_them_all.app.comment.usecase.FindCommentsByWorkId;
import com.gotta_watch_them_all.app.user_work.usecase.FindWatchedWork;
import com.gotta_watch_them_all.app.user_work.usecase.FindWorksWatchedByOneUser;
import com.gotta_watch_them_all.app.user_work.usecase.RemoveWatchedWork;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.adapter.WorkAdapter;
import com.gotta_watch_them_all.app.work.infrastructure.entrypoint.response.WorkWithDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<WorkWithDetailsResponse> saveWatchedWork(
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
        newUserWork.getWork().setComments(comments);

        return ResponseEntity.created(uri).body(WorkAdapter.toDetailResponse(newUserWork.getWork()));
    }

    @DeleteMapping("/works/{workId}")
    public ResponseEntity<UserWorkResponse> removeWatchedWork(
            @ApiIgnore @RequestAttribute("userId") String userId,
            @PathVariable("workId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long workId
    ) {
        removeWatchedWork.execute(Long.valueOf(userId), workId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/works")
    public ResponseEntity<Set<WorkWithDetailsResponse>> getWatchedWorkByCurrentUser(
            @ApiIgnore @RequestAttribute("userId") String userId
    ) {
        final var works = findWorksWatchedByOneUser.execute(Long.valueOf(userId));
        System.out.println(works);
        return ResponseEntity.ok(WorkAdapter.toDetailResponses(works));
    }

    @GetMapping("/works/{workId}")
    public ResponseEntity<UserWorkResponse> getWatchedWorkById(
            @ApiIgnore @RequestAttribute("userId") String userId,
            @PathVariable("workId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long workId
    ) {
        final var userWork = findWatchedWork.execute(Long.valueOf(userId), workId);
        return ResponseEntity.ok(UserWorkAdapter.toUserWorkResponse(userWork));
    }

    @GetMapping("/users/{userId}/works")
    public ResponseEntity<Set<WorkWithDetailsResponse>> getWatchedWorkByUser(
            @PathVariable("userId")
            @Min(value = 1, message = "id has to be equal or more than 1") Long userId
    ) {
        final var works = findWorksWatchedByOneUser.execute(userId);
        return ResponseEntity.ok(WorkAdapter.toDetailResponses(works));
    }
}
