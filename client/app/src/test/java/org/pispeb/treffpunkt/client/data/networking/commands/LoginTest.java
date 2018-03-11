package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of LoginCommand
 */

public class LoginTest extends AbstractCommandTest {

    private LoginCommand command;

    @Override
    public void initCommand() {
        command = new LoginCommand(mockName, mockPassword);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new LoginCommand.Response(mockToken, mockId));
        verify(mockSharedPref).edit();
        verify(mockEditor).putString("token", mockToken);
        verify(mockEditor).putInt("userId", mockId);
        verify(mockEditor).apply();
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        LoginCommand.Request request =
                (LoginCommand.Request) abs;

        assertEquals(request.user, mockName);
        assertEquals(request.pass, mockPassword);
        assertEquals(request.cmd, "login");
    }
}
