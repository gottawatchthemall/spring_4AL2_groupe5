package com.gotta_watch_them_all.app.unit.media.usecase;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.media.usecase.AddMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMediaTest {

    @Mock
    private MediaDao mockMediaDao;

    private AddMedia sut;

    @BeforeEach
    void setup() {
        sut = new AddMedia(mockMediaDao);
    }

    @Test
    void should_call_findByName_of_mediaDao_to_check_if_media_name_exists() throws AlreadyCreatedException {
        var mediaName = "new media name";

        sut.execute("new media name");

        verify(mockMediaDao, times(1)).findByName(mediaName);
    }

    @Test
    void when_media_already_exist_should_throw_exception() {
        String mediaToCreate = "media to create";

        when(mockMediaDao.findByName(mediaToCreate)).thenReturn(new Media().setId(1L).setName(mediaToCreate));

        assertThatThrownBy(() -> sut.execute(mediaToCreate))
                .isInstanceOf(AlreadyCreatedException.class)
                .hasMessage("Media with name '" + mediaToCreate + "' is already created");
    }

    @Test
    void should_call_createMedia_of_mediaDao() throws AlreadyCreatedException {
        String newMedia = "new media";

        when(mockMediaDao.findByName(newMedia)).thenReturn(null);

        sut.execute(newMedia);

        verify(mockMediaDao).createMedia(newMedia);
    }

    @Test
    void when_mediaDao_create_media_should_return_new_id() throws AlreadyCreatedException {
        String newMedia = "film";
        Long newMediaId = 1L;
        when(mockMediaDao.createMedia(newMedia)).thenReturn(newMediaId);

        var result = sut.execute(newMedia);

        assertThat(result).isEqualTo(newMediaId);
    }
}