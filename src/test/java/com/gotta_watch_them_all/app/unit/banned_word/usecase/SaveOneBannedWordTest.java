package com.gotta_watch_them_all.app.unit.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.banned_word.usecase.SaveOneBannedWord;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEventPublisher;
import com.gotta_watch_them_all.app.common.exception.AlreadyCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveOneBannedWordTest {
    private final String wordToBanned = "wordToBanned";
    private SaveOneBannedWord sut;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @Mock
    UpdateCommentsVulgarEventPublisher mockEventPublisher;

    @BeforeEach
    void setup() {
        sut = new SaveOneBannedWord(mockBannedWordDao, mockEventPublisher);
    }

    @Test
    void when_word_already_exists_should_throw_already_created_exception() {
        when(mockBannedWordDao.existsByWord(wordToBanned)).thenReturn(true);

        assertThatThrownBy(() -> sut.execute(wordToBanned, false))
                .isExactlyInstanceOf(AlreadyCreatedException.class)
                .hasMessage(
                        "Can't save word '%s' that is already created",
                        wordToBanned
                );
    }

    @Test
    void when_word_saved_should_return_id() throws AlreadyCreatedException {
        when(mockBannedWordDao.existsByWord(wordToBanned)).thenReturn(false);
        var savedBannedWord = new BannedWord()
                .setId(65L)
                .setWord(wordToBanned);
        when(mockBannedWordDao.saveWord(wordToBanned)).thenReturn(savedBannedWord);

        var result = sut.execute(wordToBanned, false);

        assertThat(result).isEqualTo(65L);
    }
}