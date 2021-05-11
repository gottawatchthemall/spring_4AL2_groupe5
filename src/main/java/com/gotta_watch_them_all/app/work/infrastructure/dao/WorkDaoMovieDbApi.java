package com.gotta_watch_them_all.app.work.infrastructure.dao;

import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.BadHttpRequestException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
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
        try {
            HttpRequest request = apiRequestBuilder
                    .setTitleToSearch(title)
                    .build();
            String jsonRaw = apiRequester.request(request);
            SearchMovieDbEntity search = jsonParser.toObject(jsonRaw, SearchMovieDbEntity.class);

            return search.getWorkMovieDbApiEntities()
                    .stream()
                    .map(mapper::toDomain)
                    .collect(Collectors.toSet());
        } catch (AnySearchValueFoundException | IllegalTitleGivenException | BadHttpRequestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Work findById(Long id) {
        return null;
    }

    @Override
    public Work findByImdbId(String imdbId) {
        //TODO
        return null;
    }


    @Override
    public String save(Work work) {
        return null;
    }


}
