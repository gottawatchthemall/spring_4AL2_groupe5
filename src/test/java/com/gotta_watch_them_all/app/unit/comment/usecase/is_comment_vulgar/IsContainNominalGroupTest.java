package com.gotta_watch_them_all.app.unit.comment.usecase.is_comment_vulgar;

import com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar.IsContainNominalGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Component
class IsContainNominalGroupTest {
    private IsContainNominalGroup sut;

    @BeforeEach
    void setup() {
        sut = new IsContainNominalGroup();
    }

    @Test
    void when_banned_word_is_one_word_and_comment_contain_one_banned_word_should_return_true() {
        var setBannedWord = Set.of("Fork");

        assertThat(sut.execute("a fork, yes", setBannedWord)).isTrue();
    }

    @Test
    void when_banned_word_is_one_word_and_comment_not_contain_banned_word_should_return_false() {
        var setBannedWord = Set.of("Fork");

        assertThat(sut.execute("a spoon, yes", setBannedWord)).isFalse();
    }

    @Test
    void when_banned_word_is_Fork_and_comment_contain_forked_but_not_fork_should_return_false() {
        var setBannedWord = Set.of("Fork");

        assertThat(sut.execute("forked", setBannedWord)).isFalse();
    }

    @Test
    void when_banned_word_is_Fork_and_comment_contain_fork12_but_not_fork_should_return_false() {
        var setBannedWord = Set.of("Fork");

        assertThat(sut.execute("fork12", setBannedWord)).isFalse();
    }

    @Test
    void when_banned_word_are_Fork_and_Chat_Up_and_comment_contain_on_middle_chat_up_should_return_true() {
        var setBannedWord = Set.of("Fork", "Chat Up");

        assertThat(sut.execute("When the chat up the stairs", setBannedWord)).isTrue();
    }

    @Test
    void when_banned_word_are_Fork_and_Chat_Up_and_comment_contain_on_middle_chat_upper_should_return_false() {
        var setBannedWord = Set.of("Fork", "Chat Up");

        assertThat(sut.execute("When the chat upper the stairs", setBannedWord)).isFalse();
    }

    @Test
    void when_banned_word_are_Fork_and_Chat_Up_and_comment_contain_just_fork_should_return_true() {
        var setBannedWord = Set.of("Fork", "Chat Up");

        assertThat(sut.execute("fork", setBannedWord)).isTrue();
    }

    @Test
    void when_banned_word_are_Fork_and_Chat_Up_and_comment_contain_just_chat_up_should_return_true() {
        var setBannedWord = Set.of("Fork", "Chat Up");

        assertThat(sut.execute("chat up", setBannedWord)).isTrue();
    }

    @Test
    void when_banned_word_are_Fork_and_Chat_Up_and_comment_contain_the_fork_should_return_true() {
        var setBannedWord = Set.of("Fork", "Chat Up");

        assertThat(sut.execute("the fork", setBannedWord)).isTrue();
    }

    @Test
    void when_banned_word_are_Fork_and_Chat_Up_and_comment_contain_for_with_space_should_return_false() {
        var setBannedWord = Set.of("Fork", "Chat Up");

        assertThat(sut.execute("for ", setBannedWord)).isFalse();
    }

    @Test
    void when_banned_word_is_son_of_beach_and_comment_contain_tab_in_beginning_and_son_and_space_after_should_return_false() {
        var setBannedWord = Set.of("son of beach");

        assertThat(sut.execute("\tson   ", setBannedWord)).isFalse();
    }

    @Test
    void when_banned_word_is_son_of_beach_and_comment_contain_tab_in_beginning_and_son_of_beach_and_space_after_should_return_false() {
        var setBannedWord = Set.of("son of beach");

        assertThat(sut.execute("\tson of beach ", setBannedWord)).isTrue();
    }

    @Test
    void when_banned_word_are_php_jakarta_anticonstitutionellement_and_comment_contain_spring_is_coll_should_return_false() {
        var setBannedWord = Set.of("php", "jakarta", "anticonstitutionellement");

        assertThat(sut.execute("spring is cool", setBannedWord)).isFalse();
    }
}