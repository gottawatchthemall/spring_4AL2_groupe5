package com.gotta_watch_them_all.app.unit.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.media.usecase.DeleteMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMediaTest {
    @Mock
    private MediaDao mockMediaDao;

    private DeleteMedia sut;

    @BeforeEach
    public void setup() {
        sut = new DeleteMedia(mockMediaDao);
    }

    @Test
    public void when_media_with_given_mediaId_not_exist_should_throw_exception() {
        var mediaId = 2L;
        when(mockMediaDao.existsById(mediaId)).thenReturn(false);

        assertThatThrownBy(() -> sut.execute(mediaId))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Media with id '" + mediaId + "' not found");
    }

    @Test
    public void when_media_found_should_call_mediaRepository_to_delete_found_media() throws NotFoundException {
        var mediaId = 1L;
        when(mockMediaDao.existsById(mediaId)).thenReturn(true);

        sut.execute(mediaId);

        verify(mockMediaDao, times(1)).deleteMedia(mediaId);
    }
}