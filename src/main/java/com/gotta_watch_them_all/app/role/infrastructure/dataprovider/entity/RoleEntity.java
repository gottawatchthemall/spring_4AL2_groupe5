package com.gotta_watch_them_all.app.role.infrastructure.dataprovider.entity;

import com.gotta_watch_them_all.app.role.core.entity.RoleName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity(name = "role")
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleName name;
}
