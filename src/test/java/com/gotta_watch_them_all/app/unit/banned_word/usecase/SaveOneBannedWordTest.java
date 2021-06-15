package com.gotta_watch_them_all.app.unit.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveOneBannedWordTest {
    private final String wordToBanned = "wordToBanned";
    private SaveOneBannedWord sut;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @BeforeEach
    void setup() {
        sut = new SaveOneBannedWord(mockBannedWordDao);
    }

    @Test
    void should_check_if_banned_word_already_exists() throws AlreadyCreatedException {
        sut.execute(wordToBanned);

        verify(mockBannedWordDao, times(1)).existsByWord(wordToBanned);
    }

    @Test
    void when_word_already_exists_should_throw_already_created_exception() {
        when(mockBannedWordDao.existsByWord(wordToBanned)).thenReturn(true);

        assertThatThrownBy(() -> sut.execute(wordToBanned))
                .isExactlyInstanceOf(AlreadyCreatedException.class)
                .hasMessage(
                        "Can't save word '%s' that is already created",
                        wordToBanned
                );
    }

    @Test
    void when_word_not_exists_should_save_new_one() throws AlreadyCreatedException {
        when(mockBannedWordDao.existsByWord(wordToBanned)).thenReturn(false);

        sut.execute(wordToBanned);

        verify(mockBannedWordDao, times(1)).save(wordToBanned);
    }
}