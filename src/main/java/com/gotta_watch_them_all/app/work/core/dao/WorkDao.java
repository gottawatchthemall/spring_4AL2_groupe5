package com.gotta_watch_them_all.app.work.core.dao;

import com.gotta_watch_them_all.app.work.core.entity.Work;

import java.util.Set;

public interface WorkDao {
    Set<Work> findAllByTitle(String title);

    Work findById(String id);
}
