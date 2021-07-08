package com.gotta_watch_them_all.app.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarById;
import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateCommentVulgarByIdListener implements ApplicationListener<UpdateCommentVulgarByIdEvent> {
    private final UpdateCommentVulgarById updateCommentVulgarById;
    private final UpdateUsersVulgarEventPublisher updateUsersVulgarEventPublisher;

    @Override
    public void onApplicationEvent(UpdateCommentVulgarByIdEvent event) {
        var updateComment = updateCommentVulgarById.execute(event.getCommentId());
        var setUserId = Set.of(updateComment.getUserId());
        updateUsersVulgarEventPublisher.publishEvent(setUserId);
    }
}
