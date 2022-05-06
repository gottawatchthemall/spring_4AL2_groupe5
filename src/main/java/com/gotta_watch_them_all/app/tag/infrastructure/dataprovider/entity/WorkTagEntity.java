package com.gotta_watch_them_all.app.tag.infrastructure.dataprovider.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "work_tag")
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WorkTagEntity implements Serializable {

    @Id
    @Column(name = "work_id")
    private Long workId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;
}
