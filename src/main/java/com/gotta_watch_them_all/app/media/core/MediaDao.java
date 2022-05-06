package com.gotta_watch_them_all.app.media.core;

import java.util.List;

public interface MediaDao {
    List<Media> findAll();

    Media findById(Long mediaId);

    Media findByName(String name);

    Long createMedia(String name);

    void deleteMedia(Long mediaId);

    boolean existsById(Long mediaId);

    void deleteAll();

    List<Media> saveAll(List<Media> mediaList);
}
