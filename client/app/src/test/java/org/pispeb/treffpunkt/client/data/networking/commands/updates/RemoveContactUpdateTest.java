package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by matth on 11.03.2018.
 */
public class RemoveContactUpdateTest extends AbstractUpdateTest {
    @Test
    public void applyUpdate() throws Exception {
        RemoveContactUpdate update = new RemoveContactUpdate(date, creator1);
        update.applyUpdate(mockRepos);
        verify(mockUserRepository).setIsFriend(creator1, false);
    }

}