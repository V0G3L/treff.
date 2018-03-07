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
        CreateEventCommandTest.class,
        CreateGroupCommandTest.class,
        DeleteAccountCommandTest.class,
        EditEmailCommandTest.class,
        EditEventCommandTest.class,
        EditGroupCommandTest.class,
        EditMembershipCommandTest.class,
        EditPasswordCommandTest.class,
        EditUsernameCommandTest.class,
        GetContactListCommandTest.class,
        GetGroupDetailsCommandTest.class,
        GetMembershipDetailsCommandTest.class,
        GetPollDetailsCommandTest.class,
        GetUserDetailsCommandTest.class,
        GetUserIdCommandTest.class,
        JoinEventCommandTest.class,
        LeaveEventCommandTest.class,
        LoginCommandTest.class,
        RegisterCommandTest.class,
        RejectContactRequestCommandTest.class,
        RemoveContactCommandTest.class,
        RemoveEventCommandTest.class,
        RemoveGroupMembersCommandTest.class,
        RequestUpdatesCommandTest.class,
        SendChatMessageCommandTest.class,
        SendContactRequestCommandTest.class,
        UnblockAccountCommandTest.class,
        UpdatePositionCommandTest.class
})
public class CommandTestSuite {
}
