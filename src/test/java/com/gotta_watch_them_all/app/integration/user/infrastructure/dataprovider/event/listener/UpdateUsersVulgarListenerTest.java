package com.gotta_watch_them_all.app.integration.user.infrastructure.dataprovider.event.listener;

import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import com.gotta_watch_them_all.app.user.usecase.UpdateUsersVulgar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UpdateUsersVulgarListenerTest {
    @MockBean
    private UpdateUsersVulgar mockUpdateUsersVulgar;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void when_update_users_vulgar_event_raise_should_call_listener_and_usecase_to_update_users_vulgar() {
        var setUserId = Set.of(16L, 17L);

        applicationEventPublisher.publishEvent(new UpdateUsersVulgarEvent(applicationEventPublisher, setUserId));

        verify(mockUpdateUsersVulgar, times(1)).execute(setUserId);
    }
}