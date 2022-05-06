package com.gotta_watch_them_all.app.user.core.dao;

import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.user.core.entity.User;

import java.util.Set;

public interface UserDao {
    Long createUser(String username, String email, String password, Set<Role> roles);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findById(Long userId);

    Set<User> findAllById(Set<Long> setUserId);

    Set<User> saveAll(Set<User> setUser);

    Set<User> findAll();

    Set<User> findAllBySearchingName(String username);
}
