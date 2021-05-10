package com.gotta_watch_them_all.app.work.unit.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkMovieDbApiEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMovieDbApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkMovieDbApiMapperTest {

    private WorkMovieDbApiMapper sut;


    @BeforeEach
    public void setup() {
        sut = new WorkMovieDbApiMapper();
    }

    @Test
    public void toDomain_should_return_empty_object_if_entity_null() {
        assertEquals(new Work(), sut.toDomain(null));
    }

    @Test
    public void toDomain_should_return_object_with_right_values() {
        WorkMovieDbApiEntity entity = new WorkMovieDbApiEntity()
                .setImdbID("example")
                .setTitle("title")
                .setType("movie")
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        Work expected = new Work()
                .setId("example")
                .setTitle("title")
                .setMedia(new Media().setName("Film"))
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        assertEquals(expected, sut.toDomain(entity));
    }

    @Test
    public void mapMedia_should_return_object_with_media_type_film() {
        WorkMovieDbApiEntity entity = new WorkMovieDbApiEntity()
                .setImdbID("example")
                .setTitle("title")
                .setType("movie")
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        Work expected = new Work()
                .setId("example")
                .setTitle("title")
                .setMedia(new Media().setName("Film"))
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        assertEquals(expected, sut.toDomain(entity));
    }

    @Test
    public void mapMedia_should_return_object_with_media_type_serie() {
        WorkMovieDbApiEntity entity = new WorkMovieDbApiEntity()
                .setImdbID("example")
                .setTitle("title")
                .setType("series")
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        Work expected = new Work()
                .setId("example")
                .setTitle("title")
                .setMedia(new Media().setName("Série"))
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        assertEquals(expected, sut.toDomain(entity));
    }

    @Test
    public void mapMedia_should_return_object_with_media_type_episode() {
        WorkMovieDbApiEntity entity = new WorkMovieDbApiEntity()
                .setImdbID("example")
                .setTitle("title")
                .setType("episode")
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        Work expected = new Work()
                .setId("example")
                .setTitle("title")
                .setMedia(new Media().setName("Episode"))
                .setYear("2019")
                .setPoster("https://lienversuneimage.fr");

        assertEquals(expected, sut.toDomain(entity));
    }

}
