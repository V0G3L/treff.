package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.descriptions.UsergroupEditDescription;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of EditGroupCommand
 */

public class EditGroupTest extends AbstractCommandTest {

    private EditGroupCommand command;
    private UsergroupEditDescription mockUsergroupEditDescription =
            new UsergroupEditDescription(mockGroupId, mockName);
    private UserGroup mockUsergroup = new UserGroup(mockGroupId, mockName);

    @Override
    public void initCommand() {
        command = new EditGroupCommand(mockGroupId, mockName, mockToken, mockUserGroupRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new EditGroupCommand.Response());
        verify(mockUserGroupRepository).updateGroup(mockUsergroup);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        EditGroupCommand.Request request =
                (EditGroupCommand.Request) abs;

        assertTrue(request.group.equals(mockUsergroupEditDescription));
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "edit-group");
    }
}
