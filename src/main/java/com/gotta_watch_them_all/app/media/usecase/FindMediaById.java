package com.gotta_watch_them_all.app.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class FindMediaById {
    private final MediaDao mediaDao;

    public Media execute(Long mediaId) throws NotFoundException {
        return Optional.ofNullable(mediaDao.findById(mediaId))
                .orElseThrow(getNotFoundException(mediaId));
    }

    private Supplier<NotFoundException> getNotFoundException(Long mediaId) {
        var exceptionMessage = String.format("Media with id '%d' not found", mediaId);
        return () -> new NotFoundException(exceptionMessage);
    }
}
