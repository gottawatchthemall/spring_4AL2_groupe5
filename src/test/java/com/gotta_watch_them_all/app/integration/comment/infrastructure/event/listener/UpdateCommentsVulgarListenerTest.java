package com.gotta_watch_them_all.app.integration.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class UpdateCommentsVulgarListenerTest {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void test() {
        applicationEventPublisher.publishEvent(new UpdateCommentsVulgarEvent(this));
    }
}