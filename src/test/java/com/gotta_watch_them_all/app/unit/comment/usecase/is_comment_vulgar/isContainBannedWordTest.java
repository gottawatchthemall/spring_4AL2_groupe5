package com.gotta_watch_them_all.app.unit.comment.usecase.is_comment_vulgar;

import com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar.IsContainBannedWord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class isContainBannedWordTest {
    private IsContainBannedWord sut;

    @BeforeEach
    void setup() {
        sut = new IsContainBannedWord();
    }

    @Test
    void when_comment_not_contain_banned_word_should_return_false() {

        var setBannedWord = Set.of("not", "bannedWord");

        assertThat(sut.execute("content 1", setBannedWord)).isFalse();
    }

    @Test
    void when_comment_contain_banned_word_should_return_true() {
        var setBannedWord = Set.of("content", "bannedWord");

        assertThat(sut.execute("content 1", setBannedWord)).isTrue();
    }


    @ParameterizedTest
    @NullAndEmptySource
    void when_empty_comment_should_return_false(String emptyComment) {
        var setBannedWord = Set.of("content", "bannedWord");

        assertThat(sut.execute(emptyComment, setBannedWord)).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void when_empty_set_banned_word_should_return_false(Set<String> nullOrEmptyBannedWord) {
        assertThat(sut.execute("content", nullOrEmptyBannedWord)).isFalse();
    }

    @Test
    void when_empty_comment_and_empty_set_banned_word_should_return_false() {
        assertThat(sut.execute("", Set.of())).isFalse();
    }
}