package com.gotta_watch_them_all.app.unit.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.media.usecase.FindAllMedias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllMediasTest {

    @Mock
    MediaDao mockMediaDao;

    FindAllMedias sut;

    @BeforeEach
    void setup() {
        sut = new FindAllMedias(mockMediaDao);
    }

    @Test
    void should_call_mediaDao_to_find_all_medias() {
        sut.execute();
        verify(mockMediaDao, times(1)).findAll();
    }

    @Test
    void should_return_list_medias_find_by_mediaDao() {
        Media filmMedia = new Media()
                .setId(1L)
                .setName("film");
        Media seriesMedia = new Media()
                .setId(2L)
                .setName("series");
        List<Media> mediaList = Arrays.asList(filmMedia, seriesMedia);
        when(mockMediaDao.findAll()).thenReturn(mediaList);

        var result = sut.execute();

        assertThat(result).isEqualTo(mediaList);
    }
}