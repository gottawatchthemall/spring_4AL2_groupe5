package com.gotta_watch_them_all.app.user.usecase;

import com.gotta_watch_them_all.app.user.core.dao.UserDao;
import com.gotta_watch_them_all.app.user.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchUsersByName {
    private final UserDao userdao;

    public Set<User> execute(String username) {
        return userdao.findAllBySearchingName(username);
    }
}
