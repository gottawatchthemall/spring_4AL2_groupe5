package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarPropertyEvent;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCommentVulgarPropertyListener implements ApplicationListener<UpdateCommentVulgarPropertyEvent> {
    private final UpdateCommentVulgarProperty updateCommentVulgarProperty;

    @Override
    public void onApplicationEvent(UpdateCommentVulgarPropertyEvent event) {
        updateCommentVulgarProperty.execute();
    }
}
