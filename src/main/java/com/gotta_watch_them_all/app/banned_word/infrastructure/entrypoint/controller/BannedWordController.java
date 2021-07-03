package com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import com.gotta_watch_them_all.app.banned_word.usecase.DeleteBannedWordById;
import com.gotta_watch_them_all.app.banned_word.usecase.FindAllBannedWords;
import com.gotta_watch_them_all.app.banned_word.usecase.FindOneBannedWordById;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
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
import java.util.Set;

import static org.springframework.http.ResponseEntity.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/banned-word")
public class BannedWordController {
    private final SaveOneBannedWord saveOneBannedWord;
    private final FindOneBannedWordById findOneBannedWordById;
    private final FindAllBannedWords findAllBannedWords;
    private final DeleteBannedWordById deleteBannedWordById;

    @SneakyThrows
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<URI> saveBannedWord(
            @Valid @RequestBody SaveBannedWordRequest request,
            @RequestParam(name = "update_comment", required = false, defaultValue = "false") Boolean updateComment
    ) {
        System.out.println(updateComment);
        var bannedWordId = saveOneBannedWord.execute(request.getWord(), updateComment);

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
        var foundBannedWord = findOneBannedWordById.execute(bannedWordId);
        return ok(foundBannedWord);
    }

    @GetMapping
    public ResponseEntity<Set<BannedWord>> findAll() {
        var setBannedWord = findAllBannedWords.execute();
        return ok(setBannedWord);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(
            @PathVariable("id")
            @Min(value = 1, message = "id has to be equal or more than 1") Long bannedWordId,
            @RequestParam(name = "update_comment", defaultValue = "false") Boolean updateComment
    ) throws NotFoundException {
        deleteBannedWordById.execute(bannedWordId, updateComment);
        return noContent().build();
    }
}
