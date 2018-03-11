package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of SendContactRequestCommand
 */


//TODO check username usage

public class SendContactRequestTest extends AbstractCommandTest {

    private SendContactRequestCommand command;

    @Override
    public void initCommand() {
        command = new SendContactRequestCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new SendContactRequestCommand.Response());
        verify(mockUserRepository).setIsPending(mockId, true);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        SendContactRequestCommand.Request request =
                (SendContactRequestCommand.Request) abs;


        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "send-contact-request");
    }
}
