package com.gotta_watch_them_all.app.work.usecase;

import com.gotta_watch_them_all.app.work.core.dao.WorkDao;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.IllegalTitleGivenException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FindWorkByTitleFromApi {

    private final WorkDao workDao;

    public Set<Work> execute(String title) throws NotFoundException, IllegalTitleGivenException {
        if (title == null || title.isBlank()) {
            throw new IllegalTitleGivenException("Title to search should not be null or empty");
        }

        Set<Work> works = workDao.findAllByTitle(title);

        if (works == null || works.size() < 1) {
            throw new NotFoundException(String.format("No work found for title : %s", title));
        }
        return works;
    }
}
