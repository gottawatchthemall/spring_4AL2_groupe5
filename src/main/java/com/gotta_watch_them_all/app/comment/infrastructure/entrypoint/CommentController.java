package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.infrastructure.security.CommentCreator;
import com.gotta_watch_them_all.app.comment.usecase.*;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
@Validated
public class CommentController {

    private final CreateComment createComment;
    private final FindAllComment findAllComment;
    private final DeleteCommentById deleteCommentById;
    private final UpdateCommentById updateCommentById;
    private final FindOneCommentById findOneCommentById;
    private final FindCommentsByWorkId findCommentsByWorkId;

    @PostMapping
    public ResponseEntity<Long> createComment(
            @Valid @RequestBody CreateCommentRequest comment,
            @ApiIgnore @RequestAttribute("userId") String userId
    ) {
        var commentId = createComment.execute(comment.getContent(), Long.parseLong(userId), comment.getWorkId());
        var uid = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(commentId)
                .toUri();
        return created(uid).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Comment> findById(
            @PathVariable("id") Long commentId
    ) throws NotFoundException {
        var foundComment = findOneCommentById.execute(commentId);
        return ok(foundComment);
    }

    @GetMapping("work/{workId}")
    public ResponseEntity<List<Comment>> findByWorkId(
            @PathVariable("workId") Long workId
    ) {
        var comments = findCommentsByWorkId.execute(workId);

        var sortedComments = comments.stream()
                .sorted(Comparator.comparing(Comment::getPublishAt))
                .collect(Collectors.toList());

        return ok(sortedComments);
    }

    @GetMapping
    public ResponseEntity<Set<Comment>> findAll() {
        var comments = findAllComment.execute();
        return ok(comments);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') || @commentCreator.check(#commentId, #userId)")
    public ResponseEntity<?> deleteById(
            @PathVariable("id") Long commentId,
            @ApiIgnore @RequestAttribute("userId") String userId
    ) throws NotFoundException {

        deleteCommentById.execute(commentId);

        return noContent().build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') || @commentCreator.check(#commentId, #userId)")
    public ResponseEntity<URI> updateById(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody UpdateComment request,
            @ApiIgnore @RequestAttribute("userId") String userId
    ) {
        var updateComment = updateCommentById.execute(commentId, request.getContent());

        var uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/comment/{id}")
                .buildAndExpand(
                        Map.of("id", updateComment.getId())
                ).toUri();

        return created(uri).build();
    }
}
