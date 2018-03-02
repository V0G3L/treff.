package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RegisterCommand
 */

public class RegisterTest extends AbstractCommandTest {

    private RegisterCommand command;

    @Override
    public void initCommand() {
        command = new RegisterCommand(mockName, mockPassword);
    }

    @Override
    public void onResponseTest() {
        //Doesnt work because of shared preferences
        command.onResponse(new RegisterCommand.Response(mockToken, mockId));
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RegisterCommand.Request request =
                (RegisterCommand.Request) abs;

        assertEquals(request.user, mockName);
        assertEquals(request.pass, mockPassword);
        assertEquals(request.cmd, "register");
    }
}
