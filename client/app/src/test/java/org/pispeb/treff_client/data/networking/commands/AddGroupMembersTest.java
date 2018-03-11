package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by matth on 28.02.2018.
 */

public class AddGroupMembersTest extends AbstractCommandTest {

    private AddGroupMembersCommand command;

    @Override
    public void initCommand() {
        command = new AddGroupMembersCommand(mockGroupId, mockUsers, mockToken, mockUserGroupRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new AddGroupMembersCommand.Response());
        verify(mockUserGroupRepository).addGroupMembers(mockGroupId, mockUsers);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        AddGroupMembersCommand.Request request =
                (AddGroupMembersCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.token, mockToken);
        assertArrayEquals(request.members, mockUsers);
        assertEquals(request.cmd, "add-group-members");
    }
}
