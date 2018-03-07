package org.pispeb.treff_client.data.networking.commands;

import org.mockito.Mock;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.networking.commands.descriptions.ShallowUserGroup;

import static org.junit.Assert.assertEquals;

/**
 * Test to check the main function of ListGroupsCommand
 */

public class ListGroupsTest extends AbstractCommandTest {

    @Mock
    private RequestEncoder requestEncoder;
    private ListGroupsCommand command;
    private ShallowUserGroup mockUsergroup1 = new ShallowUserGroup(1111, "checksum1");
    private ShallowUserGroup mockUsergroup2 = new ShallowUserGroup(2222, "checksum2");
    private ShallowUserGroup[] mockUsergroups = {mockUsergroup1, mockUsergroup2};

    @Override
    public void initCommand() {
        command = new ListGroupsCommand(mockToken, mockUserGroupRepository, requestEncoder);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new ListGroupsCommand.Response(mockUsergroups));
        //TODO nothing to test
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        ListGroupsCommand.Request request =
                (ListGroupsCommand.Request) abs;

        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "list-groups");
    }
}
