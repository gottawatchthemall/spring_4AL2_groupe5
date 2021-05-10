package com.gotta_watch_them_all.app.infrastructure.dao;

import com.gotta_watch_them_all.app.core.dao.UserDao;
import com.gotta_watch_them_all.app.core.entity.Role;
import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.entity.UserEntity;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.mapper.RoleMapper;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.mapper.UserMapper;
import com.gotta_watch_them_all.app.infrastructure.dataprovider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public Long createUser(String username, String email, String password, Set<Role> roles) {
        var userEntity = new UserEntity()
                .setUsername(username)
                .setEmail(email)
                .setPassword(encoder.encode(password))
                .setRoles(roles.stream().map(RoleMapper::domainToEntity).collect(Collectors.toSet()));
        var newUser = userRepository.save(userEntity);
        return newUser.getId();
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findById(Long userId) throws NotFoundException {
        if (userId == null) throw new NotFoundException("UserId should not be null");
        var userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) throw new NotFoundException(String.format("User with id %s does not exists", userId));
        return UserMapper.entityToDomain(userEntity.get());
    }
}
