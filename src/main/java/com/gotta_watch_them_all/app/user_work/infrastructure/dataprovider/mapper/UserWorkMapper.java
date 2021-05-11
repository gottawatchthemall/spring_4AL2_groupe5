package com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.entity.UserWorkEntity;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import org.springframework.stereotype.Component;

@Component
public class UserWorkMapper {
    public UserWork toDomain(UserWorkEntity entity) {
        return new UserWork()
                .setWork(new Work().setId(entity.getWorkId()))
                .setUser(new User().setId(entity.getUserId()));
    }

    public UserWorkEntity toEntity(UserWork domain) {
        return new UserWorkEntity()
                .setUserId(domain.getUser().getId())
                .setWorkId(domain.getWork().getId());
    }
}
