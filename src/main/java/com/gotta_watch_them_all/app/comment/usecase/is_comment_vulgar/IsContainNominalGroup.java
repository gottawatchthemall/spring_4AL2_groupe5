package com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class IsContainNominalGroup implements IsCommentVulgar {
    @Override
    public Boolean execute(String comment, Set<String> setBannedWord) {
        var upperComment = getTrimUppercaseComment(comment);
        var upperSetBannedWord = getTrimUppercaseSetBannedWord(setBannedWord);

        return isCommentContainBannedWordInSetBannedWord(upperComment, upperSetBannedWord);
    }

    private String getTrimUppercaseComment(String comment) {
        return comment.trim().toUpperCase();
    }

    private Set<String> getTrimUppercaseSetBannedWord(Set<String> setBannedWord) {
        return setBannedWord.stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
    }

    private boolean isCommentContainBannedWordInSetBannedWord(String upperComment, Set<String> upperSetBannedWord) {
        return upperSetBannedWord.stream()
                .anyMatch(bannedWord -> {
                    var index = upperComment.indexOf(bannedWord);
                    if (upperComment.length() > index + bannedWord.length()) {
                        return !Character.isLetterOrDigit(upperComment.charAt(index + bannedWord.length()));
                    }
                    return upperComment.contains(bannedWord);
                });
    }
}
