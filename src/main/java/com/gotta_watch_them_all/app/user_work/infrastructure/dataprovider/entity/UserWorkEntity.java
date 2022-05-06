package com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user_work")
@NoArgsConstructor
@Data
@Accessors(chain = true)
@IdClass(UserWorkEntityId.class)
public class UserWorkEntity implements Serializable {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "work_id")
    private Long workId;
}
