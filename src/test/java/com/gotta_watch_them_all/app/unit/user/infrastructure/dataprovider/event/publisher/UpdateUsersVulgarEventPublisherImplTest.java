package com.gotta_watch_them_all.app.unit.user.infrastructure.dataprovider.event.publisher;

import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.event.publisher.UpdateUsersVulgarEventPublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateUsersVulgarEventPublisherImplTest {
    private UpdateUsersVulgarEventPublisherImpl sut;

    @Mock
    private ApplicationEventPublisher mockApplicationPublisher;

    @BeforeEach
    void setup() {
        sut = new UpdateUsersVulgarEventPublisherImpl(mockApplicationPublisher);
    }

    @Test
    void should_application_should_publish_event() {
        var setUserId = Set.of(7L, 8L, 9L);

        sut.publishEvent(setUserId);

        ArgumentCaptor<UpdateUsersVulgarEvent> argument = ArgumentCaptor.forClass(UpdateUsersVulgarEvent.class);
        verify(mockApplicationPublisher, times(1)).publishEvent(argument.capture());
        assertThat(argument.getValue()).isNotNull();
        assertThat(argument.getValue().getSetUserId()).isEqualTo(setUserId);
    }
}