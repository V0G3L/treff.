package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteAccount;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AccountChangeUpdate.class})
public class AccountChangeUpdateTest extends AbstractUpdateTest {

    CompleteAccount account1 = new CompleteAccount("account", creator1, "");
    CompleteAccount account2 = new CompleteAccount("account", creator2, "");

    @Before
    public void setUp() {

        try {
            PowerMockito.whenNew(User.class).withAnyArguments().thenReturn(mockUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(mockUserRepository.getUser(creator1)).thenReturn(null);
        when(mockUserRepository.getUser(creator2)).thenReturn(mockUser);

    }

    @Test
    public void applyUpdate() throws Exception {

        AccountChangeUpdate update1 = new AccountChangeUpdate(date, creator1, account1);
        update1.applyUpdate(mockRepos);
        verify(mockUserRepository).addUser(mockUser);

        AccountChangeUpdate update2 = new AccountChangeUpdate(date, creator1, account2);
        update2.applyUpdate(mockRepos);
        verify(mockUser).setUsername(account2.username);
        verify(mockUserRepository).updateUser(mockUser);
    }

}