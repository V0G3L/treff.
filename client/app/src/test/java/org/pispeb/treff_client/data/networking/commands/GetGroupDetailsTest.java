package org.pispeb.treff_client.data.networking.commands;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteMembership;
import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteUsergroup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of GetGroupDetailsCommand
 */

public class GetGroupDetailsTest extends AbstractCommandTest {

    private GetGroupDetailsCommand command;

    @Mock
    private CompleteMembership mockCompleteMembership = mock(CompleteMembership.class);
    private int[] mockMembers = {mockId};

    private CompleteUsergroup mockCompleteUserGroup = new CompleteUsergroup
            (null, mockGroupId, mockName, mockMembers, mockEvents, mockPolls);

    @Override
    public void initCommand() {
        command = new GetGroupDetailsCommand(mockGroupId, mockToken, mockUserGroupRepository, eventRepository, encoder);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetGroupDetailsCommand.Response(mockCompleteUserGroup));
        ArgumentCaptor<UserGroup> argument = ArgumentCaptor.forClass(UserGroup.class);
        verify(mockUserGroupRepository).updateGroup(argument.capture());
        assertEquals(argument.getValue().getGroupId(), mockGroupId);
        assertEquals(argument.getValue().getName(), mockName);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        GetGroupDetailsCommand.Request request =
                (GetGroupDetailsCommand.Request) abs;

        assertEquals(request.id, mockGroupId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-group-details");
    }
}
