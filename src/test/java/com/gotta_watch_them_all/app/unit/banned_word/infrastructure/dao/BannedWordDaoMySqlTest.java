package com.gotta_watch_them_all.app.unit.banned_word.infrastructure.dao;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dao.BannedWordDaoMySql;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.entity.BannedWordEntity;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.mapper.BannedWordMapper;
import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.repository.BannedWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BannedWordDaoMySqlTest {
    private final String bannedWordStr = "bannedWordToSave";
    private BannedWordDaoMySql sut;

    @Mock
    private BannedWordRepository mockBannedWordRepository;

    private final BannedWordMapper bannedWordMapper = new BannedWordMapper();

    @BeforeEach
    void setup() {
        sut = new BannedWordDaoMySql(mockBannedWordRepository, bannedWordMapper);
    }

    @Nested
    class SaveBannedWordTest {

        @Test
        void when_word_saved_should_return_domain_saved_banned_word() {
            var bannedWordToSave = new BannedWordEntity()
                    .setWord(bannedWordStr);
            var savedBannedWord = new BannedWordEntity()
                    .setId(93L)
                    .setWord(bannedWordStr);
            when(mockBannedWordRepository.save(bannedWordToSave)).thenReturn(savedBannedWord);

            var result = sut.save(bannedWordStr);

            var expectedSavedWord = new BannedWord()
                    .setId(93L)
                    .setWord(bannedWordStr);
            assertThat(result).isEqualTo(expectedSavedWord);
        }
    }
}