package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of EditUsernameCommand
 */

public class EditUsernameTest extends AbstractCommandTest {

    private EditUsernameCommand command;

    @Override
    public void initCommand() {
        command = new EditUsernameCommand(mockName, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new EditUsernameCommand.Response());
        verify(mockSharedPref).edit();
        verify(mockEditor).putString("userName", mockName);
        verify(mockEditor).apply();
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        EditUsernameCommand.Request request =
                (EditUsernameCommand.Request) abs;

        assertEquals(request.username, mockName);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "edit-username");
    }
}
