package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

/**
 * Created by matth on 11.03.2018.
 */
public class ContactRequestAnswerUpdateTest extends AbstractUpdateTest {
    @Test
    public void applyUpdate() throws Exception {
        ContactRequestAnswerUpdate update = new ContactRequestAnswerUpdate(date, creator1, true);
        update.applyUpdate(mockRepos);
        verify(mockUserRepository).setIsPending(creator1, false);
        verify(mockUserRepository).setIsFriend(creator1, true);

    }

}