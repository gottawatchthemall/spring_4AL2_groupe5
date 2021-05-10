package com.gotta_watch_them_all.app.media.infrastructure.entrypoint;

import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.media.infrastructure.entrypoint.MediaResponse;

public class MediaAdapter {
    public static MediaResponse domainToResponse(Media media) {
        return new MediaResponse()
                .setId(media.getId())
                .setName(media.getName());
    }
}
