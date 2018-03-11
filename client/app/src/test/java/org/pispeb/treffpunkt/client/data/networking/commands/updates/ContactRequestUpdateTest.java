package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({User.class})
public class ContactRequestUpdateTest extends AbstractUpdateTest {

    @Before
    public void setUp(){
        PowerMockito.mockStatic(User.class);
        when(User.getPlaceholderAndScheduleQuery(any(int.class), any(UserRepository.class),
                any(User.UserManipulationFunction.class))).thenReturn(mockUser);

        when(mockUserRepository.getUser(creator1)).thenReturn(null);
        when(mockUserRepository.getUser(creator2)).thenReturn(mockUser);
    }

    @Test
    public void applyUpdate() throws Exception {
        ContactRequestUpdate update = new ContactRequestUpdate(date, creator1);
        update.applyUpdate(mockRepos);
        verify(User.getPlaceholderAndScheduleQuery(creator1, mockUserRepository, u -> u.setRequesting(true)));

    }

    @Test
    public void applyUpdate2() {
        ContactRequestUpdate update2 = new ContactRequestUpdate(date, creator2);
        update2.applyUpdate(mockRepos);
        verify(mockUserRepository).setIsRequesting(creator2, true);
    }

}