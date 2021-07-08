package com.gotta_watch_them_all.app.unit.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.entity.Comment;
import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.event.listener.UpdateCommentVulgarByIdListener;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarById;
import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentVulgarByIdListenerTest {
    private UpdateCommentVulgarByIdListener sut;

    @Mock
    private UpdateCommentVulgarById mockUpdateCommentVulgarById;

    @Mock
    private UpdateUsersVulgarEventPublisher mockUpdateUsersVulgarEventPublisher;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentVulgarByIdListener(mockUpdateCommentVulgarById, mockUpdateUsersVulgarEventPublisher);
    }

    @Test
    void when_call_usecase_should_publish_update_users_vulgar() {
        long commentId = 12L;
        var comment = new Comment()
                .setId(commentId)
                .setVulgar(true)
                .setContent("content")
                .setWorkId(16L)
                .setUserId(11L)
                .setUsername("username");
        when(mockUpdateCommentVulgarById.execute(commentId)).thenReturn(comment);

        sut.onApplicationEvent(new UpdateCommentVulgarByIdEvent(sut, commentId));

        verify(mockUpdateUsersVulgarEventPublisher, times(1)).publishEvent(Set.of(comment.getUserId()));
    }
}