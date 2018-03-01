package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of EditPasswordCommand
 */

public class EditPasswordTest extends AbstractCommandTest {

    private EditPasswordCommand command;

    @Override
    public void initCommand() {
        command = new EditPasswordCommand(mockPassword, mockNewPassword, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new EditPasswordCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        EditPasswordCommand.Request request =
                (EditPasswordCommand.Request) abs;

        assertEquals(request.pass, mockPassword);
        assertEquals(request.newPass, mockNewPassword);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "edit-password");
    }
}
