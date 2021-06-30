package com.gotta_watch_them_all.app.comment.usecase.is_comment_vulgar;

import java.util.Set;

public interface IsCommentVulgar {
    Boolean execute(String comment, Set<String> setBannedWord);
}
