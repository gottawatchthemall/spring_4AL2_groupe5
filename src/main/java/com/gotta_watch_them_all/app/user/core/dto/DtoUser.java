package com.gotta_watch_them_all.app.user.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DtoUser {
    private Long id;
    private String name;
    private String email;
    private boolean vulgar;
}
