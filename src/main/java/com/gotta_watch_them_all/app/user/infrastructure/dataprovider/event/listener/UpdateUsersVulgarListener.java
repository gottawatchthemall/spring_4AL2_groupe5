package com.gotta_watch_them_all.app.user.infrastructure.dataprovider.event.listener;

import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import com.gotta_watch_them_all.app.user.usecase.UpdateUsersVulgar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUsersVulgarListener implements ApplicationListener<UpdateUsersVulgarEvent> {
    private final UpdateUsersVulgar updateUsersVulgar;

    @Override
    public void onApplicationEvent(UpdateUsersVulgarEvent event) {
        updateUsersVulgar.execute(event.getSetUserId());
    }
}
