package com.gotta_watch_them_all.app.unit.work.infrastructure.dao;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.*;
import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMovieDbApi;
import com.gotta_watch_them_all.app.work.infrastructure.util.JsonParser;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.SearchMovieDbEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkMovieDbApiEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMovieDbApiMapper;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequestBuilder;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WorkDaoMovieDbApiTest {

    private WorkDaoMovieDbApi sut;

    private ApiRequester apiRequesterMock;
    private ApiRequestBuilder apiRequestBuilderMock;
    private JsonParser jsonParserMock;
    private WorkMovieDbApiMapper mapperMock;

    @BeforeEach
    public void setup() {
        apiRequesterMock = Mockito.mock(ApiRequester.class);
        apiRequestBuilderMock = Mockito.mock(ApiRequestBuilder.class);
        jsonParserMock = Mockito.mock(JsonParser.class);
        mapperMock = Mockito.mock(WorkMovieDbApiMapper.class);
        sut = new WorkDaoMovieDbApi(apiRequestBuilderMock, apiRequesterMock, jsonParserMock, mapperMock);
    }

    @Test
    public void findAllByTitle_should_call_api_req_builder_once() throws AnySearchValueFoundException, IllegalTitleGivenException, TooManySearchArgumentsException {
        Mockito.when(jsonParserMock.toObject(Mockito.any(), Mockito.any()))
                .thenReturn(new SearchMovieDbEntity().setWorkMovieDbApiEntities(new ArrayList<>()));
        Mockito.when(apiRequestBuilderMock.setTitleToSearch(Mockito.anyString())).thenReturn(apiRequestBuilderMock);
        sut.findAllByTitle("title");
        Mockito.verify(apiRequestBuilderMock, Mockito.times(1)).build();
    }

    @Test
    public void findAllByTitle_should_call_api_request_builder_set_search_title() throws IllegalTitleGivenException {
        Mockito.when(jsonParserMock.toObject(Mockito.any(), Mockito.any()))
                .thenReturn(new SearchMovieDbEntity().setWorkMovieDbApiEntities(new ArrayList<>()));
        Mockito.when(apiRequestBuilderMock.setTitleToSearch(Mockito.anyString())).thenReturn(apiRequestBuilderMock);
        sut.findAllByTitle("bonjour");
        Mockito.verify(apiRequestBuilderMock, Mockito.times(1)).setTitleToSearch("bonjour");
    }

    @Test
    public void findAllByTitle_should_call_api_req_once() throws BadHttpRequestException, IllegalTitleGivenException {
        Mockito.when(jsonParserMock.toObject(Mockito.any(), Mockito.any()))
                .thenReturn(new SearchMovieDbEntity().setWorkMovieDbApiEntities(new ArrayList<>()));
        Mockito.when(apiRequestBuilderMock.setTitleToSearch(Mockito.anyString())).thenReturn(apiRequestBuilderMock);
        sut.findAllByTitle("title");
        Mockito.verify(apiRequesterMock, Mockito.times(1)).request(Mockito.any());
    }

    @Test
    public void findAllByTitle_should_call_json_parser_once_with_work_class() throws IllegalTitleGivenException, BadHttpRequestException {
        Mockito.when(jsonParserMock.toObject(Mockito.any(), Mockito.any()))
                .thenReturn(new SearchMovieDbEntity().setWorkMovieDbApiEntities(new ArrayList<>()));
        Mockito.when(apiRequestBuilderMock.setTitleToSearch(Mockito.anyString())).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("response");
        sut.findAllByTitle("title");
        Mockito.verify(jsonParserMock, Mockito.times(1)).toObject(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void findAllByTitle_should_return_all_works_containing_title() throws IllegalTitleGivenException, AnySearchValueFoundException, BadHttpRequestException, TooManySearchArgumentsException {
        Work work1 = new Work().setImdbId("1").setTitle("Harry Potter et ceci");
        Work work2 = new Work().setImdbId("1").setTitle("Harry Potter et cela");

        WorkMovieDbApiEntity entity1 = new WorkMovieDbApiEntity().setTitle("Harry Potter et cela").setImdbID("1");
        WorkMovieDbApiEntity entity2 = new WorkMovieDbApiEntity().setTitle("Harry Potter et ceci").setImdbID("1");
        List<WorkMovieDbApiEntity> expectedWorksList = Arrays.asList(entity1, entity2);

        Set<Work> expectedWorksSet = new HashSet<>();
        expectedWorksSet.add(work1);
        expectedWorksSet.add(work2);

        Mockito.when(apiRequestBuilderMock.setTitleToSearch(Mockito.anyString())).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequestBuilderMock.build()).thenReturn(HttpRequest.newBuilder().uri(URI.create("https://local")).build());
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("response");
        Mockito.when(jsonParserMock.toObject(Mockito.any(), Mockito.any()))
                .thenReturn(new SearchMovieDbEntity().setWorkMovieDbApiEntities(expectedWorksList));

        Mockito.when(mapperMock.toBasicDomain(entity1))
                .thenReturn(work1);
        Mockito.when(mapperMock.toBasicDomain(entity2))
                .thenReturn(work2);

        assertEquals(expectedWorksSet, sut.findAllByTitle("titre"));
    }

    @Test
    public void findAllByTitle_should_return_all_works_containing_title_without_duplicates() throws IllegalTitleGivenException, AnySearchValueFoundException, BadHttpRequestException, TooManySearchArgumentsException {
        Work work1 = new Work().setImdbId("1").setTitle("Harry Potter et ceci");
        Work work2 = new Work().setImdbId("1").setTitle("Harry Potter et cela");

        WorkMovieDbApiEntity entity1 = new WorkMovieDbApiEntity().setTitle("Harry Potter et cela").setImdbID("1");
        WorkMovieDbApiEntity entity2 = new WorkMovieDbApiEntity().setTitle("Harry Potter et ceci").setImdbID("1");
        List<WorkMovieDbApiEntity> expectedWorksList = Arrays.asList(entity1, entity2, entity1, entity2);

        Set<Work> expectedWorksSet = new HashSet<>();
        expectedWorksSet.add(work1);
        expectedWorksSet.add(work2);

        Mockito.when(apiRequestBuilderMock.setTitleToSearch(Mockito.anyString())).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequestBuilderMock.build()).thenReturn(HttpRequest.newBuilder().uri(URI.create("https://local")).build());
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("response");
        Mockito.when(jsonParserMock.toObject(Mockito.any(), Mockito.any()))
                .thenReturn(new SearchMovieDbEntity().setWorkMovieDbApiEntities(expectedWorksList));

        Mockito.when(mapperMock.toBasicDomain(entity1))
                .thenReturn(work1);
        Mockito.when(mapperMock.toBasicDomain(entity2))
                .thenReturn(work2);

        assertEquals(expectedWorksSet, sut.findAllByTitle("titre"));
    }

    @Test
    public void findByImdbId_should_call_api_requester_builder_set_work_id_once() throws IllegalImdbIdGivenException, AnySearchValueFoundException, TooManySearchArgumentsException, BadHttpRequestException {
        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt404021")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("blabla");
        sut.findByImdbId("tt404021");
        Mockito.verify(apiRequestBuilderMock, Mockito.times(1))
                .setWorkIdToSearch("tt404021");
    }

    @Test
    public void findByImdbId_should_call_api_requester_builder_build_once() throws IllegalImdbIdGivenException, AnySearchValueFoundException, TooManySearchArgumentsException, BadHttpRequestException {
        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt404021")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("blabla");
        sut.findByImdbId("tt404021");
        Mockito.verify(apiRequestBuilderMock, Mockito.times(1))
                .build();
    }

    @Test
    public void findByImdbId_should_call_api_requester_once() throws IllegalImdbIdGivenException, BadHttpRequestException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt404021")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("blabla");
        sut.findByImdbId("tt404021");
        Mockito.verify(apiRequesterMock, Mockito.times(1))
                .request(Mockito.any());
    }

    @Test
    public void findByImdbId_should_call_json_parser_once() throws IllegalImdbIdGivenException, BadHttpRequestException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt404021")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("raw");
        sut.findByImdbId("tt404021");
        Mockito.verify(jsonParserMock, Mockito.times(1))
                .toObject("raw", WorkMovieDbApiEntity.class);
    }

    @Test
    public void findByImdbId_should_call_mapper_to_domain_once() throws IllegalImdbIdGivenException, BadHttpRequestException, AnySearchValueFoundException, TooManySearchArgumentsException {
        WorkMovieDbApiEntity entity = new WorkMovieDbApiEntity()
                .setImdbID("tt404021")
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
                .setType("movie")
                .setScore("50");

        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt404021")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("raw");
        Mockito.when(jsonParserMock.toObject("raw", WorkMovieDbApiEntity.class))
                .thenReturn(entity);
        sut.findByImdbId("tt404021");
        Mockito.verify(mapperMock, Mockito.times(1))
                .toFullDomain(entity);
    }


    @Test
    public void findByImdbId_should_return_work_with_all_details() throws IllegalImdbIdGivenException, BadHttpRequestException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Work expected = new Work()
                .setImdbId("tt404021")
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

        WorkMovieDbApiEntity entity = new WorkMovieDbApiEntity()
                .setImdbID("tt404021")
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
                .setType("movie")
                .setScore("50");

        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt404021")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("raw");
        Mockito.when(jsonParserMock.toObject("raw", WorkMovieDbApiEntity.class))
                .thenReturn(entity);
        Mockito.when(mapperMock.toFullDomain(entity))
                .thenReturn(expected);
        assertEquals(expected, sut.findByImdbId("tt404021"));
    }

    @Test
    public void findByImdbId_should_throw_illegal_imdbiod_exception_if_req_failed() throws IllegalImdbIdGivenException, BadHttpRequestException {
        Mockito.when(apiRequestBuilderMock.setWorkIdToSearch("tt40421")).thenReturn(apiRequestBuilderMock);
        Mockito.when(apiRequesterMock.request(Mockito.any())).thenReturn("{\"Response\":\"False\",\"Error\":\"Incorrect IMDb ID.\"}");
        assertThrows(IllegalImdbIdGivenException.class, () -> sut.findByImdbId("tt40421"));
    }


}
