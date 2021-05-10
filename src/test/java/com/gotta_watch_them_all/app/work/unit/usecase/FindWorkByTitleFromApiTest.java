package com.gotta_watch_them_all.app.work.unit.usecase;

import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMovieDbApi;
import com.gotta_watch_them_all.app.work.usecase.FindWorkByTitleFromApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FindWorkByTitleFromApiTest {

    private FindWorkByTitleFromApi sut;

    private WorkDaoMovieDbApi workDaoMovieDbApiMock;

    @BeforeEach
    public void setup() {
        workDaoMovieDbApiMock = Mockito.mock(WorkDaoMovieDbApi.class);
        sut = new FindWorkByTitleFromApi(workDaoMovieDbApiMock);
    }

    @Test
    public void execute_should_call_dao_once_with_same_title() throws NotFoundException, IllegalTitleGivenException {
        Set<Work> expectedWorks = new HashSet<>();
        expectedWorks.add(new Work().setId("1").setTitle("Harry Potter et ceci"));
        expectedWorks.add(new Work().setId("1").setTitle("Harry Potter et cela"));

        Mockito.when(workDaoMovieDbApiMock.findAllByTitle(Mockito.anyString())).thenReturn(expectedWorks);

        sut.execute("titre");
        Mockito.verify(workDaoMovieDbApiMock, Mockito.times(1)).findAllByTitle("titre");
    }

    @Test
    public void should_parse_works_from_api() throws NotFoundException, IllegalTitleGivenException {
        Set<Work> expectedWorks = new HashSet<>();
        expectedWorks.add(new Work().setId("1").setTitle("Harry Potter et ceci"));
        expectedWorks.add(new Work().setId("1").setTitle("Harry Potter et cela"));

        Mockito.when(workDaoMovieDbApiMock.findAllByTitle(Mockito.anyString())).thenReturn(expectedWorks);

        assertEquals(expectedWorks, sut.execute("title"));
    }

    @Test
    public void should_throw_not_found_exception_if_dao_returns_null() {
        Mockito.when(workDaoMovieDbApiMock.findAllByTitle(Mockito.anyString())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> sut.execute("yo"));
    }

    @Test
    public void should_throw_not_found_exception_if_dao_returns_empty_set() {
        Mockito.when(workDaoMovieDbApiMock.findAllByTitle(Mockito.anyString())).thenReturn(new HashSet<>());
        assertThrows(NotFoundException.class, () -> sut.execute("yo"));
    }

    @Test
    public void should_throw_illegal_title_given_exception_if_title_null() {
        assertThrows(IllegalTitleGivenException.class, () -> sut.execute(null));
    }

    @Test
    public void should_throw_illegal_title_given_exception_if_title_empty() {
        assertThrows(IllegalTitleGivenException.class, () -> sut.execute(" "));
    }

}
