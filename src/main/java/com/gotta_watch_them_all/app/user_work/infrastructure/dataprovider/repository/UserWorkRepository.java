package com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.repository;

import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.entity.UserWorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkRepository extends JpaRepository<UserWorkEntity, Long> {
    UserWorkEntity findByUserIdAndWorkId(Long userId, Long workId);
}
