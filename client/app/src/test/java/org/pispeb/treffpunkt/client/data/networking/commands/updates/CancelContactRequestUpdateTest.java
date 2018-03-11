package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Test;

import static org.mockito.Mockito.verify;

/**
 * Created by matth on 11.03.2018.
 */
public class CancelContactRequestUpdateTest extends AbstractUpdateTest {
    @Test
    public void applyUpdate() throws Exception {
        CancelContactRequestUpdate update = new CancelContactRequestUpdate(date, creator1);
        update.applyUpdate(mockRepos);
        verify(mockUserRepository).setIsRequesting(creator1, false);
    }

}