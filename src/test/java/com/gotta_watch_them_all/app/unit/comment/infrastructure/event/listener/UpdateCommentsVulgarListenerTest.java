package com.gotta_watch_them_all.app.unit.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.event.listener.UpdateCommentsVulgarListener;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentsVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCommentsVulgarListenerTest {
    private UpdateCommentsVulgarListener sut;

    @Mock
    private UpdateCommentsVulgar mockUpdateCommentsVulgar;
    private UpdateCommentsVulgarEvent event;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentsVulgarListener(mockUpdateCommentsVulgar);

        event = new UpdateCommentsVulgarEvent(sut);
    }

    @Test
    void should_call_usecase_update_comment_vulgar_property() {
        sut.onApplicationEvent(event);

        Mockito.verify(mockUpdateCommentsVulgar, Mockito.times(1)).execute();
    }
}