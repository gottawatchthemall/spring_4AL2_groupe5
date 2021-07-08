package com.gotta_watch_them_all.app.unit.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarById;
import com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar.IsCommentVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentVulgarByIdTest {
    private final long commentId = 98L;
    private UpdateCommentVulgarById sut;

    @Mock
    private CommentDao mockCommentDao;

    @Mock
    private BannedWordDao mockBannedWordDao;

    @Mock
    private IsCommentVulgar mockIsCommentVulgar;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentVulgarById(mockCommentDao, mockBannedWordDao, mockIsCommentVulgar);
    }

    @Test
    void when_found_comment_and_banned_words_should_check_if_comment_is_vulgar() {
        var comment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(false)
                .setUsername("user name")
                .setContent("content");
        when(mockCommentDao.findById(commentId)).thenReturn(comment);
        var setBannedWords = Set.of(
                new BannedWord().setId(3L).setWord("banned word"),
                new BannedWord().setId(6L).setWord("banned word6")
        );
        when(mockBannedWordDao.findAll()).thenReturn(setBannedWords);

        sut.execute(commentId);

        var expectedSetBannedWordContent = setBannedWords.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
        verify(mockIsCommentVulgar, times(1)).execute(comment.getContent(), expectedSetBannedWordContent);
    }

    @Test
    void when_comment_is_vulgar_should_save_comment_with_vulgar_property_to_true() {
        var comment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(false)
                .setUsername("user name")
                .setContent("content");
        when(mockCommentDao.findById(commentId)).thenReturn(comment);
        var setBannedWords = Set.of(
                new BannedWord().setId(3L).setWord("banned word"),
                new BannedWord().setId(6L).setWord("content yes")
        );
        when(mockBannedWordDao.findAll()).thenReturn(setBannedWords);
        var setStrBannedWord = setBannedWords.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
        when(mockIsCommentVulgar.execute(comment.getContent(), setStrBannedWord)).thenReturn(true);

        sut.execute(commentId);

        var expectedComment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(true)
                .setUsername("user name")
                .setContent("content");
        verify(mockCommentDao, times(1)).save(expectedComment);
    }

    @Test
    void when_comment_is_not_vulgar_should_save_comment_with_vulgar_property_to_false() {
        var comment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(false)
                .setUsername("user name")
                .setContent("content");
        when(mockCommentDao.findById(commentId)).thenReturn(comment);
        var setBannedWords = Set.of(
                new BannedWord().setId(3L).setWord("banned word"),
                new BannedWord().setId(6L).setWord("no yes")
        );
        when(mockBannedWordDao.findAll()).thenReturn(setBannedWords);
        var setStrBannedWord = setBannedWords.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
        when(mockIsCommentVulgar.execute(comment.getContent(), setStrBannedWord)).thenReturn(false);

        sut.execute(commentId);

        verify(mockCommentDao, times(1)).save(comment);
    }

    @Test
    void when_save_comment_should_return_saved_comment() {
        var comment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(false)
                .setUsername("user name")
                .setContent("content");
        when(mockCommentDao.findById(commentId)).thenReturn(comment);
        var setBannedWords = Set.of(
                new BannedWord().setId(3L).setWord("banned word"),
                new BannedWord().setId(6L).setWord("content yes")
        );
        when(mockBannedWordDao.findAll()).thenReturn(setBannedWords);
        var setStrBannedWord = setBannedWords.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
        when(mockIsCommentVulgar.execute(comment.getContent(), setStrBannedWord)).thenReturn(true);

        sut.execute(commentId);

        var expectedComment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(true)
                .setUsername("user name")
                .setContent("content");
        var savedComment = new Comment()
                .setId(3L)
                .setUserId(5L)
                .setVulgar(true)
                .setUsername("user name")
                .setContent("content");
        when(mockCommentDao.save(expectedComment)).thenReturn(savedComment);

        var result = sut.execute(commentId);

        assertThat(result).isEqualTo(savedComment);
    }
}