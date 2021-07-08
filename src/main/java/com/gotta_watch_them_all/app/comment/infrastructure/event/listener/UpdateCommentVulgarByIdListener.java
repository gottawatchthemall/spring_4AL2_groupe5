package com.gotta_watch_them_all.app.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarById;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCommentVulgarByIdListener implements ApplicationListener<UpdateCommentVulgarByIdEvent> {
    private final UpdateCommentVulgarById updateCommentVulgarById;

    @Override
    public void onApplicationEvent(UpdateCommentVulgarByIdEvent event) {
        updateCommentVulgarById.execute(event.getCommentId());
    }
}
