package com.gotta_watch_them_all.app.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentsVulgar;
import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateCommentsVulgarListener implements ApplicationListener<UpdateCommentsVulgarEvent> {
    private final UpdateCommentsVulgar updateCommentsVulgar;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onApplicationEvent(UpdateCommentsVulgarEvent event) {
        var setUpdatedComment = updateCommentsVulgar.execute();
        var setUserId = setUpdatedComment.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());

        applicationEventPublisher.publishEvent(new UpdateUsersVulgarEvent(this, setUserId));
    }
}
