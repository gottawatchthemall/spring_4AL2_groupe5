package com.gotta_watch_them_all.app.work.infrastructure.dataprovider.mapper;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.dataprovider.entity.WorkMovieDbApiEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkMovieDbApiMapper {
    public Work toFullDomain(WorkMovieDbApiEntity entity) {
        if (entity == null) return new Work();
        Work work = new Work()
                .setImdbId(entity.getImdbID())
                .setTitle(entity.getTitle())
                .setYear(entity.getYear())
                .setPoster(entity.getPoster())
                .setMedia(mapMedia(entity.getType()))
                .setReleasedDate(entity.getReleasedDate())
                .setDuration(entity.getDuration())
                .setGenres(entity.getGenres())
                .setDirectors(entity.getDirectors())
                .setWriters(entity.getWriters())
                .setActors(entity.getActors())
                .setPlot(entity.getPlot())
                .setCountry(entity.getCountry())
                .setAwards(entity.getAwards());

        if (!entity.getScore().equalsIgnoreCase("N/A")) {
            work.setScore(Integer.valueOf(entity.getScore()));
        }
        return work;
    }

    public Work toBasicDomain(WorkMovieDbApiEntity entity) {
        if (entity == null) return new Work();
        return new Work()
                .setImdbId(entity.getImdbID())
                .setTitle(entity.getTitle())
                .setYear(entity.getYear())
                .setPoster(entity.getPoster())
                .setMedia(mapMedia(entity.getType()));
    }

    private Media mapMedia(String type) {
        if (type == null || type.isBlank()) return new Media();
        if (type.equalsIgnoreCase("movie")) {
            return new Media().setName("Film");
        } else if (type.equalsIgnoreCase("series")) {
            return new Media().setName("Série");
        } else if (type.equalsIgnoreCase("episode")) {
            return new Media().setName("Episode");
        }

        return new Media();
    }


}
