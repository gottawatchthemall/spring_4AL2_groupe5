package com.gotta_watch_them_all.app.unit.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.banned_word.usecase.FindOneBannedWordById;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FindOneBannedWordByIdTest {
    private final long bannedWordId = 136L;
    private FindOneBannedWordById sut;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @BeforeEach
    void setup() {
        sut = new FindOneBannedWordById(mockBannedWordDao);
    }

    @Test
    void should_call_banned_word_dao_find_by_id() throws NotFoundException {
        sut.execute(bannedWordId);

        verify(mockBannedWordDao, times(1)).findById(bannedWordId);
    }
}