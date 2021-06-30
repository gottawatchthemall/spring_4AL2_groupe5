package com.gotta_watch_them_all.app.unit.comment.usecase;

import com.gotta_watch_them_all.app.comment.core.dao.CommentDao;
import com.gotta_watch_them_all.app.comment.usecase.UpdateCommentIfVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateCommentIfVulgarTest {
    private UpdateCommentIfVulgar sut;

    @Mock
    private CommentDao mockCommentDao;

    @BeforeEach
    void setup() {
        sut = new UpdateCommentIfVulgar(mockCommentDao);
    }

    @Test
    void should_call_comment_dao_to_get_all_comments() {
        sut.execute();

        verify(mockCommentDao, times(1)).findAll();
    }
}