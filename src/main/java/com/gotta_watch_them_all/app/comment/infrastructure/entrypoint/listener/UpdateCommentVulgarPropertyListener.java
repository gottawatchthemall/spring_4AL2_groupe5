package com.gotta_watch_them_all.app.comment.infrastructure.entrypoint.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarPropertyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateCommentVulgarPropertyListener implements ApplicationListener<UpdateCommentVulgarPropertyEvent> {
    @Override
    public void onApplicationEvent(UpdateCommentVulgarPropertyEvent event) {
        System.out.println("test");
    }
}
