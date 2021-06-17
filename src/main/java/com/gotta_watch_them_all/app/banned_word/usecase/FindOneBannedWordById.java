package com.gotta_watch_them_all.app.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindOneBannedWordById {
    private final BannedWordDao bannedWordDao;

    public BannedWord execute(Long bannedWordId) throws NotFoundException {
        return bannedWordDao.findById(bannedWordId);
    }
}
