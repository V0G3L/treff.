package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RemoveContactCommand
 */

public class RemoveContactTest extends AbstractCommandTest {

    private RemoveContactCommand command;

    @Override
    public void initCommand() {
        command = new RemoveContactCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RemoveContactCommand.Response());
        verify(mockUserRepository).setIsFriend(mockId, false);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RemoveContactCommand.Request request =
                (RemoveContactCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "remove-contact");
    }
}
