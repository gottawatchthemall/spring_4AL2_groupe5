package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkMySqlMapper {
    public Work toDomain(WorkEntity entity) {
        if (entity == null) return new Work();
        return new Work()
                .setId(entity.getId())
                .setImdbId(entity.getImdbId())
                .setTitle(entity.getTitle())
                .setYear(entity.getYear())
                .setReleasedDate(entity.getReleasedDate())
                .setDuration(entity.getDuration())
                .setGenres(entity.getGenres())
                .setDirectors(entity.getDirectors())
                .setWriters(entity.getWriters())
                .setActors(entity.getActors())
                .setPlot(entity.getPlot())
                .setCountry(entity.getCountry())
                .setAwards(entity.getAwards())
                .setPoster(entity.getPoster())
                //TODO Change by find media by id ?
                .setMedia(new Media().setId(entity.getMediaId()))
                .setScore(entity.getScore());
    }


}
