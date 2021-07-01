package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar.IsCommentVulgar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateCommentVulgarProperty {
    private final CommentDao commentDao;
    private final BannedWordDao bannedWordDao;
    private final IsCommentVulgar isCommentVulgar;

    public void execute() {
        var setComment = commentDao.findAll();
        var setBannedWord = bannedWordDao.findAll();
        var setContentBannedWord = setBannedWord.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
        setComment.forEach(comment -> {
            var hasBannedWord = isCommentVulgar.execute(comment.getContent(), setContentBannedWord);
            comment.setVulgar(hasBannedWord);
        });

        commentDao.saveAll(setComment);
    }

    private boolean isArrayCommentWordContainArrayBannedWord(Comment comment, String bannedWord) {
        var splitBannedWord = Arrays.stream(bannedWord.split(" "))
                .collect(Collectors.toList());
        var splitCommentWord = Arrays.stream(comment.getContent().split(" "))
                .collect(Collectors.toList());
        var firstBannedWord = splitCommentWord.indexOf(splitBannedWord.get(0));
        return false;
    }
}
