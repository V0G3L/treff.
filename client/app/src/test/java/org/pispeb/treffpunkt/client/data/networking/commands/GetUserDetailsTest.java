package org.pispeb.treffpunkt.client.data.networking.commands;

import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteAccount;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of GetUserDetailsCommand
 */

public class GetUserDetailsTest extends AbstractCommandTest {

    private GetUserDetailsCommand command;
    private CompleteAccount mockAccount = new CompleteAccount(null, mockId, mockName);

    @Override
    public void initCommand() {
        command = new GetUserDetailsCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetUserDetailsCommand.Response(mockAccount));
        verify(mockUserRepository).setUserName(mockId, mockName);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        GetUserDetailsCommand.Request request =
                (GetUserDetailsCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-user-details");
    }
}
