package com.gotta_watch_them_all.app.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.media.core.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllMedias {
    private final MediaDao mediaDao;

    public List<Media> execute() {
        return mediaDao.findAll();
    }
}
