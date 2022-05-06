package com.gotta_watch_them_all.app.helper;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthHelperData {
    private Long userId;
    private String jwtToken;
}
