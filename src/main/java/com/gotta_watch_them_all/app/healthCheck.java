package com.gotta_watch_them_all.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class healthCheck {

    @GetMapping("/test")
    public ResponseEntity<String> healthcheck(){
        return ok("i'm fine");
    }
}
