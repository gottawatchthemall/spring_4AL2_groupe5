package com.gotta_watch_them_all.app.unit.media.infrastructure.dao;

import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.media.infrastructure.dao.MediaDaoImpl;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaEntity;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaMapper;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaDaoImplTest {

    @Mock
    private MediaRepository mockMediaRepository;

    private MediaDaoImpl sut;

    @BeforeEach
    void setup() {
        sut = new MediaDaoImpl(mockMediaRepository);
    }

    @Nested
    class FindAll {
        @Test
        void should_call_findAll_of_mediaRepository() {
            sut.findAll();
            verify(mockMediaRepository, times(1)).findAll();
        }

        @Test
        void should_return_media_list() {
            MediaEntity filmMedia = new MediaEntity()
                    .setId(1L)
                    .setName("film")
                    ;
            MediaEntity seriesMedia = new MediaEntity()
                    .setId(2L)
                    .setName("series")
                    ;
            List<MediaEntity> mediaList = Arrays.asList(filmMedia, seriesMedia);
            List<Media> expectedList = mediaList.stream()
                    .map(MediaMapper::entityToDomain)
                    .collect(Collectors.toList());
            when(mockMediaRepository.findAll()).thenReturn(mediaList);

            var result = sut.findAll();
            assertThat(expectedList.size()).isEqualTo(expectedList.size());
            assertThat(result).isEqualTo(expectedList);
        }
    }

    @Nested
    class FindById {
        @Test
        void should_call_findById_of_mediaRepository() {
            var mediaId = 1L;
            sut.findById(mediaId);
            verify(mockMediaRepository, times(1)).findById(mediaId);
        }

        @Test
        void when_media_found_by_mediaRepository_should_return_media() {
            var mediaId = 1L;
            var filmMediaEntity = new MediaEntity().setId(mediaId).setName("film");
            when(mockMediaRepository.findById(mediaId)).thenReturn(Optional.of(filmMediaEntity));
            var expected = MediaMapper.entityToDomain(filmMediaEntity);

            var result = sut.findById(mediaId);

            assertThat(result).isEqualTo(expected);
        }

        @Test
        void when_media_not_found_return_null() {
            var mediaId = 1L;
            when(mockMediaRepository.findById(mediaId)).thenReturn(Optional.empty());

            assertThat(sut.findById(mediaId)).isNull();
        }
    }

    @Nested
    class FindByName {
        @Test
        void should_call_mediaRepository_to_find_media_by_name() {
            var mediaName = "film";

            sut.findByName(mediaName);

            verify(mockMediaRepository, times(1)).findOneByName(mediaName);
        }

        @Test
        void given_mediaName_when_media_found_should_return_concerned_media() {
            var mediaName = "movie";
            var movieMedia = new MediaEntity()
                    .setId(1L)
                    .setName(mediaName)
                    ;
            var expected = MediaMapper.entityToDomain(movieMedia);
            when(mockMediaRepository.findOneByName(mediaName)).thenReturn(movieMedia);

            var result = sut.findByName(mediaName);

            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    class CreateMedia {

        @Test
        void when_new_media_saved_should_return_the_new_media_id() {
            String newMediaName = "film";
            MediaEntity mediaEntityToSave = new MediaEntity()
                    .setName(newMediaName)
                    ;
            Long newMediaId = 3L;
            MediaEntity savedMediaEntity = new MediaEntity()
                    .setId(newMediaId)
                    .setName(newMediaName);

            when(mockMediaRepository.save(mediaEntityToSave)).thenReturn(savedMediaEntity);

            var result = sut.createMedia(newMediaName);

            assertThat(result).isEqualTo(newMediaId);
        }
    }

    @Nested
    class DeleteMedia {

        @Test
        void should_call_mediaRepository_to_delete_media_by_id() {
            var mediaId = 1L;

            sut.deleteMedia(mediaId);

            verify(mockMediaRepository, times(1)).deleteById(mediaId);
        }
    }

    @Nested
    class ExistsById {

        @Test
        void should_call_mediaRepository_to_check_if_media_exists_by_id() {
            var mediaId = 1L;

            sut.existsById(mediaId);

            verify(mockMediaRepository, times(1)).existsById(mediaId);
        }

        @ParameterizedTest
        @ValueSource(booleans = {false, true})
        void should_return_result_of_mediaRepository(boolean ifExists) {
            var mediaId = 1L;
            when(mockMediaRepository.existsById(mediaId)).thenReturn(ifExists);

            var result = sut.existsById(mediaId);

            assertThat(result).isEqualTo(ifExists);
        }
    }
}