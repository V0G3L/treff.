package org.pispeb.treff_client.data.networking.commands;

import android.arch.lifecycle.MutableLiveData;

import org.mockito.Mock;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.networking.commands.descriptions
        .ShallowUserGroup;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test to check the main function of ListGroupsCommand
 */

public class ListGroupsTest extends AbstractCommandTest {

    private ListGroupsCommand command;
    private ShallowUserGroup mockUsergroup1
            = new ShallowUserGroup("type", 1111,"checksum1");
    private ShallowUserGroup mockUsergroup2
            = new ShallowUserGroup("type", 2222,"checksum2");
    private ShallowUserGroup[] mockUsergroups
            = {mockUsergroup1, mockUsergroup2};

    @Mock
    private RequestEncoder mockEncoder;

    @Override
    public void initCommand() {
        command = new ListGroupsCommand(mockToken, mockUserGroupRepository,
                mockEncoder);
    }

    @Override
    public void onResponseTest() {

        when(mockUserGroupRepository.getGroupLiveData(mockUsergroups[0].id))
                .thenReturn(new MutableLiveData<>());
        when(mockUserGroupRepository.getGroupLiveData(mockUsergroups[1].id))
                .thenReturn(null);

        command.onResponse(new ListGroupsCommand.Response(mockUsergroups));
        verify(mockUserGroupRepository, times(2)).getGroupLiveData(anyInt());
        verify(mockEncoder).getGroupDetails(mockUsergroups[1].id);
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
