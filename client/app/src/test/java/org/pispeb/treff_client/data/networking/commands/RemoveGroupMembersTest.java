package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RemoveGroupMembersCommand
 */

public class RemoveGroupMembersTest extends AbstractCommandTest {

    private RemoveGroupMembersCommand command;

    @Override
    public void initCommand() {
        command = new RemoveGroupMembersCommand(mockGroupId, mockUsers, mockToken, mockUserGroupRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RemoveGroupMembersCommand.Response());
        verify(mockUserGroupRepository).removeGroupMembers(mockGroupId, mockUsers);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RemoveGroupMembersCommand.Request request =
                (RemoveGroupMembersCommand.Request) abs;

        assertEquals(request.id, mockGroupId);
        assertArrayEquals(request.members, mockUsers);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "remove-group-members");
    }
}
