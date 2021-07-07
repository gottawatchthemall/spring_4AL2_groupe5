package com.gotta_watch_them_all.app.user.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.user.core.dto.DtoUser;
import com.gotta_watch_them_all.app.user.usecase.FindAllUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final FindAllUser findAllUser;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DtoUser> findAll(
            @RequestParam("vulgar") Boolean isVulgar
    ) {
        findAllUser.execute(isVulgar);
        return null;
    }
}
