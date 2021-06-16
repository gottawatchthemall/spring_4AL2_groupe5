package com.gotta_watch_them_all.app.banned_word.infrastructure.bootstrap;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BannedWordBootstrap {
    private final BannedWordDao bannedWordDao;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var startListBannedWord = List.of(
                "php", "laravel", "symphony",
                "aLaFinDuFilmDeDemonSlayerIlSePasse",
                "vba"
        );

        startListBannedWord.forEach(bannedWord -> {
            if (bannedWordDao.existsByWord(bannedWord)) {
                return;
            }
            bannedWordDao.save(bannedWord);
        });
    }
}
