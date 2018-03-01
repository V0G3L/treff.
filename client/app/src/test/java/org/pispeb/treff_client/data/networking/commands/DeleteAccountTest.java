package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of DeleteAccountCommand
 */

public class DeleteAccountTest extends AbstractCommandTest {

    private DeleteAccountCommand command;

    @Override
    public void initCommand() {
        command = new DeleteAccountCommand(mockPassword, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new DeleteAccountCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        DeleteAccountCommand.Request request =
                (DeleteAccountCommand.Request) abs;

        assertEquals(request.pass, mockPassword);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "delete-account");
    }
}
