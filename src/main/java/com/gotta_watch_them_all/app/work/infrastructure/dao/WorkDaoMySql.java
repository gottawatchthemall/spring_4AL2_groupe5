package com.gotta_watch_them_all.app.work.infrastructure.dao;

import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.BadHttpRequestException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.SearchMovieDbEntity;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMovieDbApiMapper;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMySqlMapper;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.repository.WorkRepository;
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

    private final WorkRepository workRepository;
    private final WorkMySqlMapper workMySqlMapper;

    @Override
    public Set<Work> findAllByTitle(String title) {
        return null;
    }

    @Override
    public Work findById(Long id) {
        return null;
    }

    @Override
    public Work findByImdbId(String imdbId) throws IllegalImdbIdGivenException, NotFoundException {
        if (imdbId == null || imdbId.isBlank()) {
            throw new IllegalImdbIdGivenException("ImdbId can not be null or empty");
        }
        var workEntity = workRepository.findByImdbId(imdbId);
        if (workEntity == null) {
            throw new NotFoundException(String.format("Work with imdbId %s does not exists", imdbId));
        }
        return workMySqlMapper.toDomain(workEntity);
    }

    @Override
    public Work save(Work work) {
        var workEntity = workMySqlMapper.toEntity(work);
        var newWork = workRepository.save(workEntity);
        return workMySqlMapper.toDomain(newWork);
    }
}
