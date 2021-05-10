package com.gotta_watch_them_all.app.media.infrastructure.dataprovider;

import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaEntity;

public class MediaMapper {
    public static Media entityToDomain(MediaEntity entity) {
        return new Media()
                .setId(entity.getId())
                .setName(entity.getName());
    }

    public static MediaEntity domainToEntity(Media domain) {
        return new MediaEntity()
                .setId(domain.getId())
                .setName(domain.getName());
    }
}
