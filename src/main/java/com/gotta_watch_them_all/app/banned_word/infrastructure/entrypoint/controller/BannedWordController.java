package com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.banned_word.infrastructure.entrypoint.request.SaveBannedWordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/blacklisted-word")
public class BannedWordController {

    @PostMapping
    public ResponseEntity<URI> saveBlacklistedWord(SaveBannedWordRequest request) {
        return null;
    }
}
