package com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity(name = "banned_word")
@Data
@Accessors(chain = true)
public class BannedWordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String word;
}
