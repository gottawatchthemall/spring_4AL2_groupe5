package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateCommentIfVulgar {
    private final CommentDao commentDao;
    private final BannedWordDao bannedWordDao;

    public void execute() {
        var setComment = commentDao.findAll();
        var setBannedWord = bannedWordDao.findAll();
        var setContentBannedWord = setBannedWord.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
        setComment.forEach(comment -> {
            var hasBannedWord = setContentBannedWord.stream().anyMatch(bannedWord -> comment.getContent().contains(bannedWord));
            comment.setVulgar(hasBannedWord);
        });
        commentDao.saveAll(setComment);
    }
}
