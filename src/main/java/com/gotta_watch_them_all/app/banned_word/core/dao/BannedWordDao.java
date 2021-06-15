package com.gotta_watch_them_all.app.banned_word.core.dao;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;

public interface BannedWordDao {
    Boolean existsByWord(String word);

    BannedWord save(String word);
}
