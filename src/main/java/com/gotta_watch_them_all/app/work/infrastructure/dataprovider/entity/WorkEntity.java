package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity(name = "work")
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String author;

    @Column(name = "media_id")
    private Long mediaId;
}
