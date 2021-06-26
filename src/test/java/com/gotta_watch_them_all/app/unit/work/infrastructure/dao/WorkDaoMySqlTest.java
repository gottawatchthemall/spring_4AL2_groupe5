package com.gotta_watch_them_all.app.unit.work.infrastructure.dao;


import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.media.core.Media;
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

    @Test
    public void save_should_call_mapper_once() {
        Work work = new Work()
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
        Mockito.when(mockWorkRepository.save(Mockito.any())).thenReturn(new WorkEntity());
        sut.save(work);
        Mockito.verify(mockWorkMySqlMapper, Mockito.times(1)).toEntity(work);
    }

    @Test
    public void save_should_call_repo_save_once() {
        Work work = new Work()
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
                .setMedia(new Media().setId(1L));
        WorkEntity entity = new WorkEntity()
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
                .setMediaId(1L);
        Mockito.when(mockWorkMySqlMapper.toEntity(work)).thenReturn(entity);
        Mockito.when(mockWorkRepository.save(entity)).thenReturn(entity);
        sut.save(work);
        Mockito.verify(mockWorkRepository, Mockito.times(1)).save(entity);
    }

    @Test
    public void save_should_mapper_to_domain_once() {
        Work work = new Work()
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
                .setMedia(new Media().setId(1L));

        Work newWork = new Work()
                .setId(55L)
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
                .setMedia(new Media().setId(1L));

        WorkEntity entityBeforeSave = new WorkEntity()
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
                .setMediaId(1L);

        WorkEntity entityAfterSave = new WorkEntity()
                .setId(55L)
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
                .setMediaId(1L);
        Mockito.when(mockWorkMySqlMapper.toEntity(work)).thenReturn(entityBeforeSave);
        Mockito.when(mockWorkMySqlMapper.toDomain(entityAfterSave)).thenReturn(newWork);
        Mockito.when(mockWorkRepository.save(entityBeforeSave)).thenReturn(entityAfterSave);
        sut.save(work);
        Mockito.verify(mockWorkMySqlMapper, Mockito.times(1)).toDomain(entityAfterSave);
    }

    @Test
    public void save_should_return_new_work() {
        Work work = new Work()
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
                .setMedia(new Media().setId(1L));

        Work newWork = new Work()
                .setId(55L)
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
                .setMedia(new Media().setId(1L));

        WorkEntity entityBeforeSave = new WorkEntity()
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
                .setMediaId(1L);

        WorkEntity entityAfterSave = new WorkEntity()
                .setId(55L)
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
                .setMediaId(1L);
        Mockito.when(mockWorkMySqlMapper.toEntity(work)).thenReturn(entityBeforeSave);
        Mockito.when(mockWorkMySqlMapper.toDomain(entityAfterSave)).thenReturn(newWork);
        Mockito.when(mockWorkRepository.save(entityBeforeSave)).thenReturn(entityAfterSave);
        assertEquals(newWork, sut.save(work));
    }

}
