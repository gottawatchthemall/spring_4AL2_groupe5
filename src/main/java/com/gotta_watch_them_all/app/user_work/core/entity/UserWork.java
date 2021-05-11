package com.gotta_watch_them_all.app.user_work.core.entity;


import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserWork {
    private User user;
    private Work work;
}
