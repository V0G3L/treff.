package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of EditEmailCommand
 */

public class EditEmailTest extends AbstractCommandTest {

    private EditEmailCommand command;

    @Override
    public void initCommand() {
        command = new EditEmailCommand(mockEmail, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new EditEmailCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        EditEmailCommand.Request request =
                (EditEmailCommand.Request) abs;

        assertEquals(request.email, mockEmail);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "edit-email");
    }
}
