package com.gotta_watch_them_all.app.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FindAllBannedWords {
    private final BannedWordDao bannedWordDao;
    public Set<BannedWord> execute() {
        return bannedWordDao.findAll();
    }
}
