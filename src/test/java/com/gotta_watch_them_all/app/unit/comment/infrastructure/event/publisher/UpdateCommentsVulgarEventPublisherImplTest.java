package com.gotta_watch_them_all.app.unit.comment.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.event.publisher.UpdateCommentsVulgarEventPublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentsVulgarEventPublisherImplTest {
    private UpdateCommentsVulgarEventPublisherImpl sut;

    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentsVulgarEventPublisherImpl(mockApplicationEventPublisher);
    }

    @Test
    void should_call_application_publisher_with_param_update_comments_vulgar_event() {
        sut.publishEvent();

        verify(mockApplicationEventPublisher, times(1)).publishEvent(any(UpdateCommentsVulgarEvent.class));
    }
}