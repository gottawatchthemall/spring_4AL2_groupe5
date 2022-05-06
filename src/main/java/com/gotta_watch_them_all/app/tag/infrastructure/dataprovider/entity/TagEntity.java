package com.gotta_watch_them_all.app.tag.infrastructure.dataprovider.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity(name = "tag")
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;
}
