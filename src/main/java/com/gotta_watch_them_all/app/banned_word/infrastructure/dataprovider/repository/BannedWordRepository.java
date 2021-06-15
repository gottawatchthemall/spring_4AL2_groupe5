package com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.repository;

import com.gotta_watch_them_all.app.banned_word.infrastructure.dataprovider.entity.BannedWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedWordRepository extends JpaRepository<BannedWordEntity, Long> {
    Boolean existsByWord(String word);
}
