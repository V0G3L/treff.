package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treffpunkt.client.data.entities.GroupMembership;

/**
 * Created by matth on 11.03.2018.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({AccountChangeUpdateTest.class,
        CancelContactRequestUpdateTest.class,
        ChatUpdateTest.class,
        ContactRequestAnswerUpdateTest.class,
        ContactRequestUpdateTest.class,
        EventDeletionUpdateTest.class,
        GroupMembershipChangeUpdateTest.class,
        PositionChangeUpdateTest.class,
        RemoveContactUpdateTest.class,
        UsergroupChangeUpdateTest.class,
})
public class UpdatesTestSuite {
}
