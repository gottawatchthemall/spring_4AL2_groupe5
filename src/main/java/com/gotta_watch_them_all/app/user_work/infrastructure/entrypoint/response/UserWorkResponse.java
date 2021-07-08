package com.gotta_watch_them_all.app.user_work.infrastructure.entrypoint.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gotta_watch_them_all.app.user.core.entity.User;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@JsonDeserialize
@Accessors(chain = true)
public class UserWorkResponse {
    private User user;
    private Work work;
}
