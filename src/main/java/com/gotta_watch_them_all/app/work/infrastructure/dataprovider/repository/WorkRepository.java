package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.repository;

import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<WorkEntity, Long> {
    WorkEntity findByImdbId(String imdbId);
}
