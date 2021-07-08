package com.gotta_watch_them_all.app.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCommentVulgarByIdListener implements ApplicationListener<UpdateCommentVulgarByIdEvent> {
    @Override
    public void onApplicationEvent(UpdateCommentVulgarByIdEvent event) {

    }
}
