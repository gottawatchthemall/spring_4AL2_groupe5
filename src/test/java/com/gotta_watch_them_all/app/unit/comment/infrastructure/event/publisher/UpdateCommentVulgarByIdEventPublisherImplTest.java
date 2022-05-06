package com.gotta_watch_them_all.app.unit.comment.infrastructure.event.publisher;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.event.publisher.UpdateCommentVulgarByIdEventPublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateCommentVulgarByIdEventPublisherImplTest {
    private UpdateCommentVulgarByIdEventPublisherImpl sut;

    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentVulgarByIdEventPublisherImpl(mockApplicationEventPublisher);
    }

    @Test
    void should_call_application_event_publisher_with_instance_of_update_comment_vulgar_by_id_event() {
        sut.publishEvent(23L);

        ArgumentCaptor<UpdateCommentVulgarByIdEvent> argument = ArgumentCaptor.forClass(UpdateCommentVulgarByIdEvent.class);

        verify(mockApplicationEventPublisher, times(1)).publishEvent(argument.capture());
        assertThat(argument.getValue()).isNotNull();
        assertThat(argument.getValue().getCommentId()).isEqualTo(23);
    }
}