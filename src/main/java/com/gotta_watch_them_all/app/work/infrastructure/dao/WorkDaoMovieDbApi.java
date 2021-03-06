package com.gotta_watch_them_all.app.work.infrastructure.dao;

import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.*;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkMovieDbApiEntity;
import com.gotta_watch_them_all.app.work.infrastructure.util.JsonParser;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.SearchMovieDbEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMovieDbApiMapper;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequestBuilder;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.net.http.HttpRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("movieDbApiDao")
public class WorkDaoMovieDbApi implements WorkDao {

    private final ApiRequestBuilder apiRequestBuilder;
    private final ApiRequester apiRequester;
    private final JsonParser jsonParser;
    private final WorkMovieDbApiMapper mapper;

    @Override
    public Set<Work> findAllByTitle(String title) {
        HttpRequest request = apiRequestBuilder
                .setTitleToSearch(title)
                .build();
        String jsonRaw = apiRequester.request(request);
        SearchMovieDbEntity search = jsonParser.toObject(jsonRaw, SearchMovieDbEntity.class);

        return search.getWorkMovieDbApiEntities()
                .stream()
                .map(mapper::toBasicDomain)
                .collect(Collectors.toSet());

    }

    @Override
    public Set<Work> findAllByIds(Set<Long> ids) {
        return null;
    }


    @Override
    public Work findById(Long id) {
        return null;
    }

    @Override
    public Work findByImdbId(String imdbId) {
        var request = apiRequestBuilder
                .setWorkIdToSearch(imdbId)
                .build();

        var jsonRaw = apiRequester.request(request);
        if (jsonRaw.contains("Error")) {
            throw new IllegalImdbIdGivenException(String.format("Wrong imdbId %s", imdbId));
        }
        var entity = jsonParser.toObject(jsonRaw, WorkMovieDbApiEntity.class);
        if (!entity.getImdbID().equalsIgnoreCase(imdbId)) {
            throw new IllegalImdbIdGivenException(String.format("Wrong imdbId %s", imdbId));
        }
        return mapper.toFullDomain(entity);
    }

    @Override
    public Work save(Work work) {
        return null;
    }


}
