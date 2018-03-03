package org.pispeb.treff_server.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AcceptContactRequestCommandTest.class,
        AddGroupMembersCommandTest.class,
        BlockAccountCommandTest.class,
        CancelContactRequestCommandTest.class,
        CreateGroupCommandTest.class,
        EditGroupCommandTest.class,
        GetContactListCommandTest.class,
        GetGroupDetailsCommandTest.class,
        GetPollDetailsCommandTest.class,
        GetUserDetailsCommandTest.class,
        GetUserIdCommandTest.class,
        LoginCommandTest.class,
        RegisterCommandTest.class,
        RejectContactRequestCommandTest.class,
        RemoveContactCommandTest.class,
        RemoveGroupMembersCommandTest.class,
        SendContactRequestCommandTest.class,
        UnblockAccountCommandTest.class,
        UpdatePositionCommandTest.class
})
public class CommandTestSuite {
}
