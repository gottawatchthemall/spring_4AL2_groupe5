package com.gotta_watch_them_all.app.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.common.exception.AlreadyCreatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveOneBannedWord {
    private final BannedWordDao bannedWordDao;

    public Long execute(String word) throws AlreadyCreatedException {
        if (bannedWordDao.existsByWord(word)) {
            var message = String.format(
                    "Can't save word '%s' that is already created",
                    word
            );
            throw new AlreadyCreatedException(message);
        }
        var savedBannedWord = bannedWordDao.save(word);
        
        return savedBannedWord.getId();
    }
}
