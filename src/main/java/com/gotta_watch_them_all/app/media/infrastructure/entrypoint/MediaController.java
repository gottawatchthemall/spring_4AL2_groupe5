package com.gotta_watch_them_all.app.media.infrastructure.entrypoint;

import com.gotta_watch_them_all.app.common.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.media.usecase.AddMedia;
import com.gotta_watch_them_all.app.media.usecase.DeleteMedia;
import com.gotta_watch_them_all.app.media.usecase.FindAllMedias;
import com.gotta_watch_them_all.app.media.usecase.FindMediaById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/media")
public class MediaController {
    private final FindAllMedias findAllMedias;
    private final FindMediaById findMediaById;
    private final AddMedia addMedia;
    private final DeleteMedia deleteMedia;

    @GetMapping
    public ResponseEntity<List<MediaResponse>> findAll() {
        var medias = findAllMedias.execute().stream()
                .map(MediaAdapter::domainToResponse)
                .collect(Collectors.toList());
        return ok(medias);
    }

    @GetMapping("{id}")
    public ResponseEntity<MediaResponse> findById(
            @PathVariable("id")
            @Min(value = 1, message = "id has to be equal or more than 1") Long mediaId
    ) throws NotFoundException {
        var media = findMediaById.execute(mediaId);
        return ok(MediaAdapter.domainToResponse(media));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<URI> saveOne(@Valid @RequestBody CreateMediaRequest request) throws AlreadyCreatedException {
        var newMediaId = addMedia.execute(request.getName());
        var uid = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newMediaId)
                .toUri();
        return created(uid).build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOne(
            @PathVariable("id")
            @Min(value = 1, message = "id has to be equal or more than 1") Long mediaId
    ) throws NotFoundException {
        deleteMedia.execute(mediaId);
        return noContent().build();
    }
}
