package com.gotta_watch_them_all.app.media.infrastructure.dataprovider;

import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<MediaEntity, Long> {
    MediaEntity findOneByName(String name);
}
