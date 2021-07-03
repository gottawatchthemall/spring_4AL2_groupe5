package com.gotta_watch_them_all.app.unit.banned_word.infrastructure.bootstrap;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.banned_word.infrastructure.bootstrap.BannedWordBootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BannedWordBootstrapTest {
    private BannedWordBootstrap sut;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @Mock
    private ApplicationReadyEvent mockApplicationReadyEvent;
    private List<String> expectedListBannedWord;

    @BeforeEach
    void setup() {
        sut = new BannedWordBootstrap(mockBannedWordDao);
        expectedListBannedWord = List.of(
                "php", "laravel", "symphony",
                "aLaFinDuFilmDeDemonSlayerIlSePasse",
                "vba"
        );
    }

    @Test
    void should_check_if_list_banned_word_exists() {
        sut.on(mockApplicationReadyEvent);

        expectedListBannedWord.forEach(bannedWord -> {
            verify(mockBannedWordDao, times(1)).existsByWord(bannedWord);
        });
    }

    @Test
    void when_first_word_not_exists_should_save_first_word() {
        when(mockBannedWordDao.existsByWord("php")).thenReturn(false);
        when(mockBannedWordDao.existsByWord("laravel")).thenReturn(true);
        when(mockBannedWordDao.existsByWord("symphony")).thenReturn(true);
        when(mockBannedWordDao.existsByWord("vba")).thenReturn(true);
        when(mockBannedWordDao.existsByWord("aLaFinDuFilmDeDemonSlayerIlSePasse")).thenReturn(true);

        sut.on(mockApplicationReadyEvent);

        verify(mockBannedWordDao, times(1)).saveWord("php");
    }

    @Test
    void when_symphony_and_vba_not_exist_should_save_missed_2_words() {
        when(mockBannedWordDao.existsByWord("php")).thenReturn(true);
        when(mockBannedWordDao.existsByWord("laravel")).thenReturn(true);
        when(mockBannedWordDao.existsByWord("symphony")).thenReturn(false);
        when(mockBannedWordDao.existsByWord("aLaFinDuFilmDeDemonSlayerIlSePasse")).thenReturn(true);
        when(mockBannedWordDao.existsByWord("vba")).thenReturn(false);

        sut.on(mockApplicationReadyEvent);

        verify(mockBannedWordDao, times(1)).saveWord("symphony");
        verify(mockBannedWordDao, times(1)).saveWord("vba");
    }
}