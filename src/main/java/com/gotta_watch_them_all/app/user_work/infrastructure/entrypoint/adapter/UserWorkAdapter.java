package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.adapter;

import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.response.UserWorkResponse;

public class UserWorkAdapter {
    public static UserWorkResponse toUserWorkResponse(UserWork userWork) {
        return new UserWorkResponse()
                .setWork(userWork.getWork())
                .setUser(userWork.getUser());
    }
}
