package com.gotta_watch_them_all.app.auth.infrastructure.entrypoint;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
