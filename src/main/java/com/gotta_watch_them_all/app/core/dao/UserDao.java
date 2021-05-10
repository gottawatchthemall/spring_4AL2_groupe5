package com.gotta_watch_them_all.app.core.dao;

import com.gotta_watch_them_all.app.core.entity.Role;
import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;

import java.util.Set;

public interface UserDao {
    Long createUser(String username, String email, String password, Set<Role> roles);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findById(Long userId) throws NotFoundException;
}
