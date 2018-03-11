package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of ResetPasswordCommand
 */

public class ResetPasswordTest extends AbstractCommandTest {

    private ResetPasswordCommand command;

    @Override
    public void initCommand() {
        command = new ResetPasswordCommand(mockEmail);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new ResetPasswordCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        ResetPasswordCommand.Request request =
                (ResetPasswordCommand.Request) abs;

        assertEquals(request.email, mockEmail);
        assertEquals(request.cmd, "reset-password");
    }
}
