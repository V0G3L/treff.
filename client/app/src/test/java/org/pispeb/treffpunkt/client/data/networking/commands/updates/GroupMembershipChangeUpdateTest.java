package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.data.entities.GroupMembership;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteMembership;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({GroupMembershipChangeUpdate.class})
public class GroupMembershipChangeUpdateTest extends AbstractUpdateTest {

    @Mock
    GroupMembership mockGroupMembership = mock(GroupMembership.class);

    private CompleteMembership membership = new CompleteMembership("", group1, creator1,
            date.getTime(), new HashMap<String, Boolean>());

    @Before
    public void setUp(){
        try {
            PowerMockito.whenNew(GroupMembership.class).withArguments(creator1, group1).thenReturn(mockGroupMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void applyUpdate() throws Exception {
        GroupMembershipChangeUpdate update = new GroupMembershipChangeUpdate(date, creator1, membership);
        update.applyUpdate(mockRepos);
        verify(mockGroupMembership).setSharingUntil(date);
        verify(mockUserGroupRepository).updateMembership(mockGroupMembership);
    }

}