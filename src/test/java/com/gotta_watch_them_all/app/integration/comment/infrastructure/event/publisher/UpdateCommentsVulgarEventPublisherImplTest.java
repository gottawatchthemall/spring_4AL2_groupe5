package com.gotta_watch_them_all.app.integration.comment.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEventPublisher;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentsVulgar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UpdateCommentsVulgarEventPublisherImplTest {
    @Autowired
    private UpdateCommentsVulgarEventPublisher sut;

    @MockBean
    private UpdateCommentsVulgar mockUpdateCommentsVulgar;

    @Test
    void should_call_updateCommentsVulgar_on_listener() {
        sut.publishEvent();

        verify(mockUpdateCommentsVulgar, times(1)).execute();
    }
}