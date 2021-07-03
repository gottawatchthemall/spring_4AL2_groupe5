package com.gotta_watch_them_all.app.unit.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentsVulgarEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.event.listener.UpdateCommentsVulgarListener;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentsVulgar;
import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentsVulgarListenerTest {
    private UpdateCommentsVulgarListener sut;

    @Mock
    private UpdateCommentsVulgar mockUpdateCommentsVulgar;

    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    private UpdateCommentsVulgarEvent event;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentsVulgarListener(mockUpdateCommentsVulgar, mockApplicationEventPublisher);

        event = new UpdateCommentsVulgarEvent(sut);
    }

    @Test
    void should_call_usecase_update_comment_vulgar_property() {
        sut.onApplicationEvent(event);

        verify(mockUpdateCommentsVulgar, times(1)).execute();
    }

    @Test
    void when_update_comments_vulgar_should_publish_update_users_vulgar_event() {
        var comment1 = new Comment()
                .setId(2L)
                .setUserId(45L)
                .setWorkId(13L)
                .setContent("content comment 1")
                .setVulgar(false);
        var comment2 = new Comment()
                .setId(3L)
                .setUserId(46L)
                .setWorkId(13L)
                .setContent("content comment 2")
                .setVulgar(true);
        var setComments = Set.of(comment1, comment2);
        when(mockUpdateCommentsVulgar.execute()).thenReturn(setComments);

        sut.onApplicationEvent(event);

        var expectedSetUserId = Set.of(45L, 46L);
        verify(mockApplicationEventPublisher, times(1))
                .publishEvent(new UpdateUsersVulgarEvent(sut, expectedSetUserId));
    }
}