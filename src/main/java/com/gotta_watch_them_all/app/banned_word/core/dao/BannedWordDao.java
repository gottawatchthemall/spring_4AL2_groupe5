package com.gotta_watch_them_all.app.banned_word.core.dao;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;

public interface BannedWordDao {
    Boolean existsByWord(String word);

    BannedWord save(String word);

    BannedWord findById(Long bannedWordId) throws NotFoundException;
}
