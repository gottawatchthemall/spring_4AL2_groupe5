package com.gotta_watch_them_all.app.user.infrastructure.dao;

import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.role.core.entity.Role;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.entity.UserEntity;
import com.gotta_watch_them_all.app.role.infrastructure.dataprovider.mapper.RoleMapper;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.mapper.UserMapper;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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
    public User findById(Long userId) {
        if (userId == null) throw new NotFoundException("UserId should not be null");
        var userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) throw new NotFoundException(String.format("User with id %s does not exists", userId));
        return UserMapper.entityToDomain(userEntity.get());
    }
}
