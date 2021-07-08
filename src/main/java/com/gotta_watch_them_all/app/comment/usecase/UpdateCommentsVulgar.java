package com.gotta_watch_them_all.app.comment.usecase;

import com.gotta_watch_them_all.app.banned_word.core.BannedWord;
import com.gotta_watch_them_all.app.banned_word.core.dao.BannedWordDao;
import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar.IsCommentVulgar;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateCommentsVulgar {
    private final CommentDao commentDao;
    private final BannedWordDao bannedWordDao;
    @Qualifier("IsContainNominalGroup")
    private final IsCommentVulgar isCommentVulgar;

    public Set<Comment> execute() {
        var result = new HashSet<Comment>();
        var setComment = commentDao.findAll();
        var setBannedWord = bannedWordDao.findAll();
        var setContentBannedWord = getSetContentBannedWord(setBannedWord);

        updateCommentsVulgarAndAddToResultChangedOnes(result, setComment, setContentBannedWord);

        commentDao.saveAll(setComment);
        return result;
    }

    private Set<String> getSetContentBannedWord(Set<BannedWord> setBannedWord) {
        return setBannedWord.stream()
                .map(BannedWord::getWord)
                .collect(Collectors.toSet());
    }

    private void updateCommentsVulgarAndAddToResultChangedOnes(HashSet<Comment> result, Set<Comment> setComment, Set<String> setContentBannedWord) {
        setComment.forEach(comment -> {
            var isVulgar = comment.isVulgar();
            var hasBannedWord = isCommentVulgar.execute(comment.getContent(), setContentBannedWord);
            comment.setVulgar(hasBannedWord);
            if (isVulgar != hasBannedWord) {
                result.add(comment);
            }
        });
    }
}
