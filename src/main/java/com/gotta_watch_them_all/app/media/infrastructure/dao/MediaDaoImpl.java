package com.gotta_watch_them_all.app.media.infrastructure.dao;

import com.gotta_watch_them_all.app.media.core.MediaDao;
import com.gotta_watch_them_all.app.media.core.Media;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaEntity;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaMapper;
import com.gotta_watch_them_all.app.media.infrastructure.dataprovider.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaDaoImpl implements MediaDao {
    private final MediaRepository mediaRepository;

    @Override
    public List<Media> findAll() {
        var mediaEntityList = mediaRepository.findAll();
        return mediaEntityList.stream()
                .map(MediaMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Media findById(Long mediaId) {
        return mediaRepository.findById(mediaId)
                .map(MediaMapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public Media findByName(String name) {
        return Optional.ofNullable(mediaRepository.findOneByName(name))
                .map(MediaMapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public Long createMedia(String name) {
        MediaEntity newMedia = new MediaEntity().setName(name);
        return mediaRepository.save(newMedia).getId();
    }

    @Override
    public void deleteMedia(Long mediaId) {
        mediaRepository.deleteById(mediaId);
    }

    @Override
    public boolean existsById(Long mediaId) {
        return mediaRepository.existsById(mediaId);
    }

    @Override
    public void deleteAll() {
        mediaRepository.deleteAll();
    }

    @Override
    public List<Media> saveAll(List<Media> mediaList) {
        var mediaEntityList = mediaList.stream()
                .map(MediaMapper::domainToEntity)
                .collect(Collectors.toList());
        return mediaRepository.saveAll(mediaEntityList).stream()
                .map(MediaMapper::entityToDomain)
                .collect(Collectors.toList());
    }
}
