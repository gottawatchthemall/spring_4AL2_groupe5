package com.gotta_watch_them_all.app.work.infrastructure.dao;

import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.BadHttpRequestException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.SearchMovieDbEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMovieDbApiMapper;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequestBuilder;
import com.gotta_watch_them_all.app.work.infrastructure.util.ApiRequester;
import com.gotta_watch_them_all.app.work.infrastructure.util.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("mySqlDao")
public class WorkDaoMySql implements WorkDao {

    @Override
    public Set<Work> findAllByTitle(String title) {
        return null;
    }

    @Override
    public Work findById(String id) {
        return null;
    }

    @Override
    public String save(Work work) {
        return null;
    }
}
