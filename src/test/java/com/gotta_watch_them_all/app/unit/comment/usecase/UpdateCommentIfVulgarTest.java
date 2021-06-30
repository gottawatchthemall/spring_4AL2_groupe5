package com.gotta_watch_them_all.app.unit.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentIfVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentIfVulgarTest {
    private UpdateCommentIfVulgar sut;

    @Mock
    private CommentDao mockCommentDao;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentIfVulgar(mockCommentDao, mockBannedWordDao);
    }

    @Test
    void should_call_comment_dao_to_get_all_comments() {
        sut.execute();

        verify(mockCommentDao, times(1)).findAll();
    }

    @Test
    void when_get_set_comment_should_call_banned_word_dao_to_get_all_banned_word() {
        var comment1 = new Comment()
                .setId(1L)
                .setContent("content 1")
                .setUserId(61L)
                .setVulgar(false)
                .setWorkId(23L);
        var comment2 = new Comment()
                .setId(2L)
                .setContent("content 2")
                .setUserId(3L)
                .setVulgar(true)
                .setWorkId(5L);
        var setComment = Set.of(comment1, comment2);
        when(mockCommentDao.findAll()).thenReturn(setComment);

        sut.execute();

        verify(mockBannedWordDao, times(1)).findAll();
    }

    @Test
    void when_get_set_comment_and_banned_word_should_update_comment_if_contain_banned_word() {
        var comment1 = new Comment()
                .setId(1L)
                .setContent("content 1")
                .setUserId(61L)
                .setVulgar(false)
                .setWorkId(23L);
        var comment2 = new Comment()
                .setId(2L)
                .setContent("content 2 php")
                .setUserId(3L)
                .setVulgar(false)
                .setWorkId(5L);
        var comment3 = new Comment()
                .setId(3L)
                .setContent("the jakarta has to be banned")
                .setUserId(3L)
                .setVulgar(false)
                .setWorkId(4L);
        var setComment = Set.of(comment1, comment2, comment3);
        when(mockCommentDao.findAll()).thenReturn(setComment);
        var bannedWord1 = new BannedWord()
                .setId(31L)
                .setWord("php");
        var bannedWord2 = new BannedWord()
                .setId(45L)
                .setWord("jakarta");
        var setBannedWord = Set.of(bannedWord1, bannedWord2);
        when(mockBannedWordDao.findAll()).thenReturn(setBannedWord);

        sut.execute();

        var expectedComment2 = new Comment()
                .setId(2L)
                .setContent("content 2 php")
                .setUserId(3L)
                .setVulgar(true)
                .setWorkId(5L);
        var expectedComment3 = new Comment()
                .setId(3L)
                .setContent("the jakarta has to be banned")
                .setUserId(3L)
                .setVulgar(true)
                .setWorkId(4L);
        var expectedSetComment = Set.of(comment1, expectedComment2, expectedComment3);
        verify(mockCommentDao, times(1)).saveAll(expectedSetComment);
    }
}