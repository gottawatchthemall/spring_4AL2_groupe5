package com.gotta_watch_them_all.app.banned_word.core.dao;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;

import java.util.Set;

public interface BannedWordDao {
    Boolean existsByWord(String word);

    BannedWord saveWord(String word);

    BannedWord findById(Long bannedWordId) throws NotFoundException;

    Set<BannedWord> findAll();

    Boolean existsById(Long bannedWordId);

    void deleteById(Long bannedWordId);
}
