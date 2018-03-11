package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RegisterCommand
 */

public class RegisterTest extends AbstractCommandTest {

    private RegisterCommand command;

    @Override
    public void initCommand() {
        command = new RegisterCommand(mockName, mockPassword, "");
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RegisterCommand.Response(mockToken, mockId));
        verify(mockSharedPref).edit();
        verify(mockEditor).putString("token", mockToken);
        verify(mockEditor).putInt("userId", mockId);
        verify(mockEditor).commit();
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
