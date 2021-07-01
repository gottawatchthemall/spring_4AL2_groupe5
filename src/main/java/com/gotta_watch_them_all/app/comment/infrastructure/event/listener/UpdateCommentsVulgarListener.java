package com.gotta_watch_them_all.app.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentsVulgar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCommentsVulgarListener implements ApplicationListener<UpdateCommentsVulgarEvent> {
    private final UpdateCommentsVulgar updateCommentsVulgar;

    @Override
    public void onApplicationEvent(UpdateCommentsVulgarEvent event) {
        updateCommentsVulgar.execute();
    }
}
