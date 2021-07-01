package com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class IsContainEachWordOfNominalGroup implements IsCommentVulgar {
    @Override
    public Boolean execute(String comment, Set<String> setBannedWord) {
        return null;
    }
}
