package com.gotta_watch_them_all.app.user.usecase;

import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.dto.DtoUser;
import com.gotta_watch_them_all.app.user.infrastructure.entrypoint.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllUser {
    private final UserDao userdao;
    public Set<DtoUser> execute(Boolean isVulgar) {
        var setUser = userdao.findAll();

        return setUser.stream()
                .filter(user -> user.isVulgar() == isVulgar)
                .map(UserAdapter::domainToDto)
                .collect(Collectors.toSet());
    }
}