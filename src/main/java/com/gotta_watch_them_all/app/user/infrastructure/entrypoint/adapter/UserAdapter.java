package com.gotta_watch_them_all.app.user.infrastructure.entrypoint.adapter;

import com.gotta_watch_them_all.app.user.core.dto.DtoUser;
import com.gotta_watch_them_all.app.user.core.entity.User;

public class UserAdapter {
    public static DtoUser domainToDto(User domain) {
        return new DtoUser()
                .setId(domain.getId())
                .setName(domain.getName())
                .setEmail(domain.getEmail())
                .setVulgar(domain.isVulgar());
    }
}
