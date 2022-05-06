package com.gotta_watch_them_all.app.user.infrastructure.entrypoint.controller;

import com.gotta_watch_them_all.app.user.core.dto.DtoUser;
import com.gotta_watch_them_all.app.user.infrastructure.entrypoint.adapter.UserAdapter;
import com.gotta_watch_them_all.app.user.infrastructure.entrypoint.request.AddUserRequest;
import com.gotta_watch_them_all.app.user.usecase.AddUser;
import com.gotta_watch_them_all.app.user.usecase.FindAllUser;
import com.gotta_watch_them_all.app.user.usecase.SearchUsersByName;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final FindAllUser findAllUser;
    private final SearchUsersByName searchUsersByName;
    private final AddUser addUser;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<DtoUser>> findAll(
            @RequestParam("vulgar") Optional<Boolean> isVulgar
    ) {
        var dtoUsers = findAllUser.execute(isVulgar);
        return ResponseEntity.ok(dtoUsers);
    }

    @GetMapping("/search/name/{username}")
    public ResponseEntity<Set<DtoUser>> findAllByUsername(
            @PathVariable("username")
            @NotBlank(message = "Username should be specified") String username
    ) {
        final var users = searchUsersByName.execute(username);
        return ResponseEntity.ok(UserAdapter.toDtoUsers(users));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DtoUser> addUser(@Valid @RequestBody AddUserRequest request) {
        var savedUser = addUser.execute(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRoles()
        );
        return ResponseEntity.ok(savedUser);
    }
}
