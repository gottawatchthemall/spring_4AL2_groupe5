package com.gotta_watch_them_all.app.unit.comment.infrastructure.event.listener;

import com.gotta_watch_them_all.app.comment.core.event.UpdateCommentVulgarByIdEvent;
import com.gotta_watch_them_all.app.comment.infrastructure.event.listener.UpdateCommentVulgarByIdListener;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentVulgarById;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateCommentVulgarByIdListenerTest {
    private final long commentId = 12L;
    private UpdateCommentVulgarByIdListener sut;

    @Mock
    private UpdateCommentVulgarById mockUpdateCommentVulgarById;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentVulgarByIdListener(mockUpdateCommentVulgarById);
    }

    @Test
    void should_call_usecase_update_comment_vulgar_by_id() {
        sut.onApplicationEvent(new UpdateCommentVulgarByIdEvent(sut, commentId));

        verify(mockUpdateCommentVulgarById, times(1)).execute(commentId);
    }

    @Test
    void when_call_usecase_should_publish_update_users_vulgar() {

    }
}