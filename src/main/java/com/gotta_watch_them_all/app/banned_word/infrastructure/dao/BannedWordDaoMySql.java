package com.gotta_watch_them_all.app.banned_word.infrastructure.dao;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.entity.BannedWordEntity;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.mapper.BannedWordMapper;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.repository.BannedWordRepository;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannedWordDaoMySql implements BannedWordDao {
    private final BannedWordRepository bannedWordRepository;
    private final BannedWordMapper bannedWordMapper;

    @Override
    public Boolean existsByWord(String word) {
        return bannedWordRepository.existsByWord(word);
    }

    @Override
    public BannedWord save(String word) {
        var bannedWordToSave = new BannedWordEntity()
                .setWord(word);
        var savedBannedWord = bannedWordRepository.save(bannedWordToSave);
        return bannedWordMapper.toDomain(savedBannedWord);
    }

    @Override
    public BannedWord findById(Long bannedWordId) throws NotFoundException {
        return bannedWordRepository.findById(bannedWordId)
                .map(bannedWordMapper::toDomain)
                .orElseThrow(() -> {
                    var message = String.format(
                            "Banned word with id '%d' not found",
                            bannedWordId
                    );
                    return new NotFoundException(message);
                });
    }
}
