package com.gotta_watch_them_all.app.unit.work.infrastructure.dao;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMySql;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMySqlMapper;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.repository.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class WorkDaoMySqlTest {

    private WorkDaoMySql sut;

    private WorkRepository mockWorkRepository;
    private WorkMySqlMapper mockWorkMySqlMapper;

    @BeforeEach
    public void setup() {
        mockWorkRepository = Mockito.mock(WorkRepository.class);
        mockWorkMySqlMapper = Mockito.mock(WorkMySqlMapper.class);
        sut = new WorkDaoMySql(mockWorkRepository, mockWorkMySqlMapper);
    }

    @Test
    public void findByImdbId_should_call_repository_once() throws IllegalImdbIdGivenException, NotFoundException {
        Mockito.when(mockWorkRepository.findByImdbId("bonjour")).thenReturn(new WorkEntity());
        sut.findByImdbId("bonjour");
        Mockito.verify(mockWorkRepository, Mockito.times(1)).findByImdbId("bonjour");
    }

    @Test
    public void findByImdbId_should_throw_illegal_imdbid_exception_if_imdbid_null() {
        assertThrows(IllegalImdbIdGivenException.class, () -> sut.findByImdbId(null));
    }

    @Test
    public void findByImdbId_should_throw_illegal_imdbid_exception_if_imdbid_blank() {
        assertThrows(IllegalImdbIdGivenException.class, () -> sut.findByImdbId("    "));
    }

    @Test
    public void findByImdbId_should_call_work_sql_mapper_once() throws IllegalImdbIdGivenException, NotFoundException {
        Mockito.when(mockWorkRepository.findByImdbId("bonjour")).thenReturn(new WorkEntity());
        sut.findByImdbId("bonjour");
        Mockito.verify(mockWorkMySqlMapper, Mockito.times(1)).toDomain(Mockito.any());
    }

    @Test
    public void findByImdbId_should_throw_not_found_exception_if_imdbid_not_found() {
        Mockito.when(mockWorkRepository.findByImdbId("bjr")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> sut.findByImdbId("bjr"));
    }

    @Test
    public void findByImdbId_should_return_work_model() throws IllegalImdbIdGivenException, NotFoundException {
        Work expectedWork = new Work()
                .setId(1L)
                .setImdbId("bjr")
                .setTitle("bjr")
                .setYear("2222")
                .setReleasedDate("demain")
                .setDuration("150min")
                .setGenres("action")
                .setDirectors("action")
                .setWriters("action")
                .setActors("action")
                .setPlot("action")
                .setCountry("action")
                .setAwards("action")
                .setPoster("action")
                .setMedia(new Media().setId(1L))
                .setScore(50);

        WorkEntity entity = new WorkEntity()
                .setId(1L)
                .setImdbId("bjr")
                .setTitle("bjr")
                .setYear("2222")
                .setReleasedDate("demain")
                .setDuration("150min")
                .setGenres("action")
                .setDirectors("action")
                .setWriters("action")
                .setActors("action")
                .setPlot("action")
                .setCountry("action")
                .setAwards("action")
                .setPoster("action")
                .setMediaId(1L)
                .setScore(50);

        Mockito.when(mockWorkRepository.findByImdbId("bjr")).thenReturn(entity);
        Mockito.when(mockWorkMySqlMapper.toDomain(entity)).thenReturn(expectedWork);

        assertEquals(expectedWork, sut.findByImdbId("bjr"));
    }

}