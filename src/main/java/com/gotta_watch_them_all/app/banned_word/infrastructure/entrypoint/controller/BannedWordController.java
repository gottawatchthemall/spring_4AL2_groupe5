package com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banned-word")
public class BannedWordController {
    private final SaveOneBannedWord saveOneBannedWord;

    @SneakyThrows
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<URI> saveBlacklistedWord(@Valid @RequestBody SaveBannedWordRequest request) {
        var bannedWordId = saveOneBannedWord.execute(request.getWord());

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bannedWordId)
                .toUri();
        return created(uri).build();
    }
}
