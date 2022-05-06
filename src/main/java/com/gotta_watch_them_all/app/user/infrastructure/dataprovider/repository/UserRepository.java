package com.gotta_watch_them_all.app.user.infrastructure.dataprovider.repository;

import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<UserEntity> findByUsernameContaining(String username);
}
