package com.gotta_watch_them_all.app.unit.banned_word.usecase;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.banned_word.usecase.DeleteBannedWordById;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEventPublisher;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteBannedWordByIdTest {
    private final long bannedWordId = 61L;
    private DeleteBannedWordById sut;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @Mock
    private UpdateCommentsVulgarEventPublisher mockEventPublisher;

    @BeforeEach
    void setup() {
        sut = new DeleteBannedWordById(mockBannedWordDao, mockEventPublisher);
    }

    @Test
    void when_banned_word_not_exists_should_send_not_found_exception() {
        when(mockBannedWordDao.existsById(bannedWordId)).thenReturn(false);

        assertThatThrownBy(() -> sut.execute(bannedWordId, false))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Banned word with id '%d' not found", bannedWordId);
    }

    @Test
    void when_banned_word_exists_should_delete_banned_word_by_id() throws NotFoundException {
        when(mockBannedWordDao.existsById(bannedWordId)).thenReturn(true);

        sut.execute(bannedWordId, false);

        verify(mockBannedWordDao, times(1)).deleteById(bannedWordId);
    }

    @Test
    void when_update_comment_param_is_false_should_not_publish_event() {
        when(mockBannedWordDao.existsById(bannedWordId)).thenReturn(true);

        sut.execute(bannedWordId, false);

        verify(mockEventPublisher, never()).publishEvent();
    }

    @Test
    void when_update_comment_param_is_true_should_publish_event() {
        when(mockBannedWordDao.existsById(bannedWordId)).thenReturn(true);

        sut.execute(bannedWordId, true);

        verify(mockEventPublisher, times(1)).publishEvent();
    }
}