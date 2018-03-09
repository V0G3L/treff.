package org.pispeb.treff_client.data.networking.commands;


import org.mockito.ArgumentCaptor;
import org.pispeb.treff_client.data.entities.User;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of GetUserIdCommand
 */

public class GetUserIdTest extends AbstractCommandTest {

    private GetUserIdCommand command;

    @Override
    public void initCommand() {
        command = new GetUserIdCommand(mockName, mockToken,
                mockUserRepository, mockEncoder);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetUserIdCommand.Response(mockId));

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(mockUserRepository).addUser(argument.capture());
        assertEquals(argument.getValue().getUserId(), mockId);
        assertEquals(argument.getValue().getUsername(), mockName);
        assertFalse(argument.getValue().isFriend());
        assertFalse(argument.getValue().isBlocked());
        assertFalse(argument.getValue().isRequesting());
        assertFalse(argument.getValue().isRequestPending());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        GetUserIdCommand.Request request =
                (GetUserIdCommand.Request) abs;

        assertEquals(request.user, mockName);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-user-id");
    }
}
