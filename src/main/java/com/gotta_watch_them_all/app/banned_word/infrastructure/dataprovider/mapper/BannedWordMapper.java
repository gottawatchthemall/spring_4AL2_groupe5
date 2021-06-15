package com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.entity.BannedWordEntity;
import org.springframework.stereotype.Component;

@Component
public class BannedWordMapper {
    public BannedWord toDomain(BannedWordEntity entity) {
        return new BannedWord()
                .setId(entity.getId())
                .setWord(entity.getWord());
    }
}
