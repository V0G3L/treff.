package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of CancelContactRequestCommand
 */

public class CancelContactRequestTest extends AbstractCommandTest {

    private CancelContactRequestCommand command;

    @Override
    public void initCommand() {
        command = new CancelContactRequestCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new CancelContactRequestCommand.Response());
        verify(mockUserRepository).setIsRequesting(mockId, false);
        verify(mockUserRepository).setIsFriend(mockId, false);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        CancelContactRequestCommand.Request request =
                (CancelContactRequestCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "cancel-contact-request");
    }
}
