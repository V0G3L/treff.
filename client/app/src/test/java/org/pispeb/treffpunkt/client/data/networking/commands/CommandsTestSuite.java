package org.pispeb.treffpunkt.client.data.networking.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All command test classes
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AcceptContactRequestTest.class,
        AddGroupMembersTest.class,
        AddPollOptionTest.class,
        BlockAccountTest.class,
        CancelContactRequestTest.class,
        CreateEventTest.class,
        CreateGroupTest.class,
        CreatePollTest.class,
        DeleteAccountTest.class,
        EditEmailTest.class,
        EditEventTest.class,
        EditGroupTest.class,
        EditPasswordTest.class,
        EditPollTest.class,
        EditUsernameTest.class,
        GetContactListTest.class,
        GetEventDetailsTest.class,
        GetGroupDetailsTest.class,
        GetPermissionsTest.class,
        GetPollDetailsTest.class,
        GetUserDetailsTest.class,
        GetUserIdTest.class,
        JoinEventTest.class,
        LeaveEventTest.class,
        ListGroupsTest.class,
        LoginTest.class,
        PublishPositionTest.class,
        RegisterTest.class,
        RejectContactRequestTest.class,
        RemoveContactTest.class,
        RemoveEventTest.class,
        RemoveGroupMembersTest.class,
        RemovePollOptionTest.class,
        RemovePollTest.class,
        RequestPositionTest.class,
        ResetPasswordConfirmTest.class,
        ResetPasswordTest.class,
        SendChatMessageTest.class,
        SendContactRequestTest.class,
        UnblockAccountTest.class,
        UpdatePositionTest.class,
        VoteForOptionTest.class,
        WithdrawVoteForOptionTest.class
})
public class CommandsTestSuite {
}
