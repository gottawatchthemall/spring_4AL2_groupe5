package com.gotta_watch_them_all.app.unit.comment.infrastructure.entrypoint.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarPropertyEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.entrypoint.listener.UpdateCommentVulgarPropertyListener;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCommentVulgarPropertyListenerTest {
    private UpdateCommentVulgarPropertyListener sut;

    @Mock
    private UpdateCommentVulgarProperty mockUpdateCommentVulgarProperty;
    private UpdateCommentVulgarPropertyEvent event;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentVulgarPropertyListener(mockUpdateCommentVulgarProperty);

        event = new UpdateCommentVulgarPropertyEvent(sut);
    }

    @Test
    void should_call_usecase_update_comment_vulgar_property() {
        sut.onApplicationEvent(event);

        Mockito.verify(mockUpdateCommentVulgarProperty, Mockito.times(1)).execute();
    }
}