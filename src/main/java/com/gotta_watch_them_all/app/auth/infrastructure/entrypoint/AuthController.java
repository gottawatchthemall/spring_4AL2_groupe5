package com.gotta_watch_them_all.app.auth.infrastructure.entrypoint;

import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.auth.infrastructure.entrypoint.LoginRequest;
import com.gotta_watch_them_all.app.auth.infrastructure.entrypoint.SignupRequest;
import com.gotta_watch_them_all.app.auth.infrastructure.entrypoint.JwtResponse;
import com.gotta_watch_them_all.app.infrastructure.entrypoint.response.MessageResponse;
import com.gotta_watch_them_all.app.auth.infrastructure.security.JwtUtils;
import com.gotta_watch_them_all.app.auth.infrastructure.security.UserDetailsImpl;
import com.gotta_watch_them_all.app.auth.usecase.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SignUp signUp;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        var jwtResponse = new JwtResponse()
                .setToken(jwt)
                .setId(userDetails.getId())
                .setUsername(userDetails.getUsername())
                .setEmail(userDetails.getEmail())
                .setRoles(roles);
        return ResponseEntity.ok(
                jwtResponse
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws NotFoundException, AlreadyCreatedException {
        signUp.execute(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                signupRequest.getRoles()
        );

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
