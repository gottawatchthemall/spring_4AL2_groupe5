package com.gotta_watch_them_all.app.work.infrastructure.dao;

import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper.WorkMySqlMapper;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
    public Set<Work> findAllByIds(Set<Long> ids) {
        return new HashSet<>(workMySqlMapper.toDomainList(workRepository.findAllById(ids)));
    }

    @Override
    public Work findById(Long id) {
        final var work = workRepository.findById(id);
        return work.map(workMySqlMapper::toDomain).orElse(null);
    }

    @Override
    public Work findByImdbId(String imdbId) {
        if (imdbId == null || imdbId.isBlank()) {
            throw new IllegalImdbIdGivenException("ImdbId can not be null or empty");
        }
        var workEntity = workRepository.findByImdbId(imdbId);
        if (workEntity == null) return null;
        return workMySqlMapper.toDomain(workEntity);
    }

    @Override
    public Work save(Work work) {
        var workEntity = workMySqlMapper.toEntity(work);
        var newWork = workRepository.save(workEntity);
        return workMySqlMapper.toDomain(newWork);
    }
}
