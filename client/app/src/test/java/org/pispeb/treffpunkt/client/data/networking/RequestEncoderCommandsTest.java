package org.pispeb.treffpunkt.client.data.networking;

import org.junit.Test;
import org.pispeb.treffpunkt.client.data.networking.commands.CmdDesc;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;

/**
 * Test all the commands provided by the RequestEncoder
 */

public class RequestEncoderCommandsTest extends RequestEncoderTestHelper {

    @Test
    public void registerTest() {
        testEncoder.register(mockUserName, mockPassword);
        verify(mockConnectionHandler).sendMessage(contains("\"cmd\":\"" +
                CmdDesc.REGISTER.toString() + "\""));
    }

    @Test
    public void loginTest() {
        testEncoder.login(mockUserName, mockPassword);
        verify(mockConnectionHandler).sendMessage(contains("\"cmd\":\"" +
                CmdDesc.LOGIN.toString() + "\""));
    }

    // TODO all the other tests, focus in the ones actually used
    @Test
    public void editUsernameTest() {
    }

    @Test
    public void editEmailTest() {
    }

    @Test
    public void editPasswordTest() {
    }

    @Test
    public void resetPasswordTest() {
    }

    @Test
    public void resetPasswordConfirmTest() {
    }

    @Test
    public void deleteAccountTest() {
    }

    @Test
    public void getUserIdTest() {
    }

    @Test
    public void sendContactRequestTest() {
    }

    @Test
    public void cancelContactRequestTest() {
    }

    @Test
    public void acceptContactRequestTest() {
    }

    @Test
    public void rejectContactRequestTest() {
    }

    @Test
    public void removeContactTest() {
    }

    @Test
    public void getContactListTest() {
    }

    @Test
    public void blockAccountTest() {
    }

    @Test
    public void unblockAccountTest() {
    }

    @Test
    public void createGroupTest() {
    }

    @Test
    public void editGroupTest() {
    }

    @Test
    public void addGroupMembersTest() {
    }

    @Test
    public void removeGroupMembersTest() {
    }

    @Test
    public void getPermissionsTest() {
    }

    @Test
    public void createEventTest() {
    }

    @Test
    public void editEventTest() {
    }

    @Test
    public void joinEventTest() {
    }

    @Test
    public void leaveEventTest() {
    }

    @Test
    public void removeEventTest() {
    }

    @Test
    public void createPollTest() {
    }

    @Test
    public void editPollTest() {
    }

    @Test
    public void addPollOptionTest() {
    }

    @Test
    public void editPollOptionTest() {
    }

    @Test
    public void voteForOptionTest() {
    }

    @Test
    public void withdrawVoteForOptionTest() {
    }

    @Test
    public void removePollOptionTest() {
    }

    @Test
    public void removePollTest() {
    }

    @Test
    public void sendChatMessageTest() {
    }

    @Test
    public void listGroupsTest() {
    }

    @Test
    public void getGroupDetailsTest() {

    }
    @Test
    public void getUserDetailsTest() {
    }

    @Test
    public void getEventDetailsTest() {
    }

    @Test
    public void getPollDetailsTest() {
    }

    @Test
    public void requestPositionTest() {
    }

    @Test
    public void updatePositionTest() {
    }

    @Test
    public void publishPositionTest() {
    }
}
