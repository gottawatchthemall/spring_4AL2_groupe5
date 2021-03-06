package com.gotta_watch_them_all.app.helper;

import com.gotta_watch_them_all.app.auth.infrastructure.security.JwtUtils;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthHelper {
    private final UserDao userDao;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthHelperData createUserAndGetAuthData(
            String username,
            String email,
            String password,
            Set<Role> roles
    ) {
        var userId = userDao.createUser(username, email, password, roles);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtToken = jwtUtils.generateJwtToken(authentication);
        return new AuthHelperData()
                .setUserId(userId)
                .setJwtToken(jwtToken);
    }
}
