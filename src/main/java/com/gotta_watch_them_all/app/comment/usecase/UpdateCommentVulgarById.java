package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar.IsCommentVulgar;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateCommentVulgarById {
    private final CommentDao commentDao;
    private final BannedWordDao bannedWordDao;
    @Qualifier("IsContainNominalGroup")
    private final IsCommentVulgar isCommentVulgar;

    public Comment execute(Long commentId) {
        var foundComment = commentDao.findById(commentId);
        var setBannedWords = bannedWordDao.findAll();
        var setStrBannedWords = setBannedWords.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());

        var isVulgar = isCommentVulgar.execute(foundComment.getContent(), setStrBannedWords);
        foundComment.setVulgar(isVulgar);

        return commentDao.save(foundComment);
    }
}
