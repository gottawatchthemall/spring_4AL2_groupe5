package com.gotta_watch_them_all.app.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.media.core.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddMedia {
    private final MediaDao mediaDao;

    public Long execute(String name) throws AlreadyCreatedException {
        var check = mediaDao.findByName(name);
        checkIfMediaNameNotExist(name, check);
        return mediaDao.createMedia(name);
    }

    private void checkIfMediaNameNotExist(String name, Media check) throws AlreadyCreatedException {
        if (check != null) {
            var message = String.format("Media with name '%s' is already created", name);
            throw new AlreadyCreatedException(message);
        }
    }
}
