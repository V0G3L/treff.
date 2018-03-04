package org.pispeb.treff_client.data.networking.commands;

import org.pispeb.treff_client.data.networking.commands.descriptions.ShallowUserGroup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of ListGroupsCommand
 */

public class ListGroupsTest extends AbstractCommandTest {

    private ListGroupsCommad command;
    private ShallowUserGroup mockUsergroup1 = new ShallowUserGroup(1111, "checksum1");
    private ShallowUserGroup mockUsergroup2 = new ShallowUserGroup(2222, "checksum2");
    private ShallowUserGroup[] mockUsergroups = {mockUsergroup1, mockUsergroup2};

    @Override
    public void initCommand() {
        command = new ListGroupsCommad(mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new ListGroupsCommad.Response(mockUsergroups));
        //TODO nothing to test
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        ListGroupsCommad.Request request =
                (ListGroupsCommad.Request) abs;

        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "list-groups");
    }
}
