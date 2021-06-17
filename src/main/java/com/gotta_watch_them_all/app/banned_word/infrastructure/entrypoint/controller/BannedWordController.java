package com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.banned_word.usecase.FindOneBannedWordById;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/banned-word")
public class BannedWordController {
    private final SaveOneBannedWord saveOneBannedWord;
    private final FindOneBannedWordById findOneBannedWordById;

    @SneakyThrows
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<URI> saveBannedWord(@Valid @RequestBody SaveBannedWordRequest request) {
        var bannedWordId = saveOneBannedWord.execute(request.getWord());

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bannedWordId)
                .toUri();
        return created(uri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<BannedWord> findById(
            @PathVariable("id")
            @Min(value = 1, message = "id has to be equal or more than 1") Long bannedWordId
    ) throws NotFoundException {
        findOneBannedWordById.execute(bannedWordId);
        return null;
    }
}
