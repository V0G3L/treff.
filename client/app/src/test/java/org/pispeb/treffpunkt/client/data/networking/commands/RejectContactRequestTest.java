package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of RejectContactRequestCommand
 */

public class RejectContactRequestTest extends AbstractCommandTest {

    private RejectContactRequestCommand command;

    @Override
    public void initCommand() {
        command = new RejectContactRequestCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new RejectContactRequestCommand.Response());
        verify(mockUserRepository).setIsFriend(mockId, false);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        RejectContactRequestCommand.Request request =
                (RejectContactRequestCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "reject-contact-request");
    }
}
