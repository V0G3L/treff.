package org.pispeb.treffpunkt.client.data.networking.commands;

import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.UsergroupCreateDescription;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of CreateGroupCommand
 */

public class CreateGroupTest extends AbstractCommandTest {

    private CreateGroupCommand command;
    private UsergroupCreateDescription mockUsergroupCreateDescription =
            new UsergroupCreateDescription("type", mockName, mockUsers);
    private UserGroup mockUserGroup = new UserGroup(mockGroupId, mockName);
    @Override
    public void initCommand() {
        command = new CreateGroupCommand(mockName, mockUsers, mockToken, mockUserGroupRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new CreateGroupCommand.Response(mockGroupId));
        verify(mockUserGroupRepository).addGroup(mockUserGroup);
        verify(mockUserGroupRepository).addGroupMembers(mockGroupId, mockUsers);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        CreateGroupCommand.Request request =
                (CreateGroupCommand.Request) abs;

        assertTrue(request.group.equals(mockUsergroupCreateDescription));
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "create-group");
    }
}
