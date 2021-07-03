package com.gotta_watch_them_all.app.user.infrastructure.dataprovider.event.listener;

import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateUsersVulgarListener implements ApplicationListener<UpdateUsersVulgarEvent> {
    @Override
    public void onApplicationEvent(UpdateUsersVulgarEvent event) {

    }
}
