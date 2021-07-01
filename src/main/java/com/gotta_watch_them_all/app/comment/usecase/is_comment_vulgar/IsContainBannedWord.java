package com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar;


import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class IsContainBannedWord implements IsCommentVulgar {

    @Override
    public Boolean execute(String comment, Set<String> setBannedWord) {
        if (isCommentOrSetBannedWordNull(comment, setBannedWord)) {
            return false;
        }
        return isCommentContainsBannedWordInSetBannedWord(comment, setBannedWord);
    }

    private boolean isCommentOrSetBannedWordNull(String comment, Set<String> setBannedWord) {
        return comment == null || setBannedWord == null;
    }

    private boolean isCommentContainsBannedWordInSetBannedWord(String comment, Set<String> setBannedWord) {
        return setBannedWord.stream().anyMatch(comment::contains);
    }
}
