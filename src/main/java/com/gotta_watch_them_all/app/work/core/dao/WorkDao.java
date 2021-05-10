package com.gotta_watch_them_all.app.work.core.dao;

import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;

import java.util.Set;

public interface WorkDao {
    Set<Work> findAllByTitle(String title);

    Work findById(Long id);

    Work findByImdbId(String imdbId) throws IllegalImdbIdGivenException, NotFoundException;

    String save(Work work);
}
