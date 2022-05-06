package com.gotta_watch_them_all.app.unit.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.common.exception.NotFoundException;
import com.gotta_watch_them_all.app.media.usecase.FindMediaById;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMediaByIdTest {
    @Mock
    private MediaDao mockMediaDao;

    FindMediaById sut;

    @BeforeEach
    public void setup() {
        sut = new FindMediaById(mockMediaDao);
    }

    @Test
    public void when_mediaDao_not_found_media_should_throw() {
        var mediaId = 1L;
        when(mockMediaDao.findById(mediaId)).thenReturn(null);

        assertThatThrownBy(() -> sut.execute(mediaId))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Media with id '" + mediaId + "' not found");
    }

    @Test
    public void when_mediaDao_found_media_should_return_concerned_one() throws NotFoundException {
        var mediaId = 1L;
        var expectedMedia = new Media()
                .setId(mediaId)
                .setName("The media");
        when(mockMediaDao.findById(mediaId)).thenReturn(expectedMedia);

        var result = sut.execute(mediaId);

        assertThat(result).isEqualTo(expectedMedia);
    }
}