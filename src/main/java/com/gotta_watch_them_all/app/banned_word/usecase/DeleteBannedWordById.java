package com.gotta_watch_them_all.app.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEventPublisher;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBannedWordById {
    private final BannedWordDao bannedWordDao;
    private final UpdateCommentsVulgarEventPublisher updateCommentsVulgarEventPublisher;

    public void execute(Long bannedWordId, Boolean updateComment) throws NotFoundException {
        if (!bannedWordDao.existsById(bannedWordId)) {
            var message = String.format(
                    "Banned word with id '%d' not found", bannedWordId
            );
            throw new NotFoundException(message);
        }
        bannedWordDao.deleteById(bannedWordId);

        if (updateComment) {
            updateCommentsVulgarEventPublisher.publishEvent();
        }
    }
}
