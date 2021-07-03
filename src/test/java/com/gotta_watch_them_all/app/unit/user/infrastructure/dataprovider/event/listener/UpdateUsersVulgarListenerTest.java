package com.gotta_watch_them_all.app.unit.user.infrastructure.dataprovider.event.listener;

import com.gotta_watch_them_all.app.user.core.event.UpdateUsersVulgarEvent;
import com.gotta_watch_them_all.app.user.infrastructure.dataprovider.event.listener.UpdateUsersVulgarListener;
import com.gotta_watch_them_all.app.user.usecase.UpdateUsersVulgar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateUsersVulgarListenerTest {
    private UpdateUsersVulgarListener sut;

    @Mock
    private UpdateUsersVulgar mockUpdateUsersVulgar;

    @BeforeEach
    void setup() {
        sut = new UpdateUsersVulgarListener(mockUpdateUsersVulgar);
    }

    @Test
    void should_call_usecase_update_users_vulgar_with_set_user_id_of_event() {
        var setUsersId = Set.of(13L, 14L);
        var updateUsersVulgarEvent = new UpdateUsersVulgarEvent(sut, setUsersId);

        sut.onApplicationEvent(updateUsersVulgarEvent);

        verify(mockUpdateUsersVulgar, times(1)).execute(setUsersId);
    }
}