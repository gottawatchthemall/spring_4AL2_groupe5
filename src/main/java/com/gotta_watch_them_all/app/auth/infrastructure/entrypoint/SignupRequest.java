package com.gotta_watch_them_all.app.auth.infrastructure.entrypoint;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class SignupRequest {
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}
