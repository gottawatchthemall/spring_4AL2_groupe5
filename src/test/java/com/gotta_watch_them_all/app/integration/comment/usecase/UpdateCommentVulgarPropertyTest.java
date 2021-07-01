package com.gotta_watch_them_all.app.integration.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UpdateCommentVulgarPropertyTest {
    @Autowired
    private UpdateCommentVulgarProperty sut;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BannedWordDao bannedWordDao;

    @Test
    void given_2_banned_words_and_5_comments_when_3_comments_contain_one_of_banned_words_should_update_3_comments() {
        var comment1 = new Comment()
                .setContent("the php is difficult")
                .setWorkId(32L)
                .setVulgar(false)
                .setUserId(48L);
        var concernedComment1 = commentDao.save(comment1);
        var comment2 = new Comment()
                .setContent("the word jee")
                .setWorkId(32L)
                .setVulgar(false)
                .setUserId(48L);
        commentDao.save(comment2);
        var comment3 = new Comment()
                .setContent("spring is cool")
                .setWorkId(32L)
                .setVulgar(false)
                .setUserId(48L);
        commentDao.save(comment3);
        var comment4 = new Comment()
                .setContent("test php test")
                .setWorkId(72L)
                .setVulgar(false)
                .setUserId(25L);
        var concernedComment4 = commentDao.save(comment4);
        var comment5 = new Comment()
                .setContent("java phpéé")
                .setWorkId(63L)
                .setVulgar(false)
                .setUserId(32L);
        commentDao.save(comment5);

        bannedWordDao.saveWord("php");
        bannedWordDao.saveWord("jakarta");
        bannedWordDao.saveWord("anticonstitutionellement");

        sut.execute();

        var setVulgarComment = commentDao.findAll()
                .stream()
                .filter(Comment::isVulgar)
                .collect(Collectors.toSet());
        var expectedSetComment = Set.of(
                concernedComment1,
                concernedComment4
        ).stream().peek(comment -> comment.setVulgar(true))
                .collect(Collectors.toSet());

        assertThat(setVulgarComment).isEqualTo(expectedSetComment);
    }
}