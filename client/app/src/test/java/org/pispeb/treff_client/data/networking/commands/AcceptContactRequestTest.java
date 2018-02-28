package org.pispeb.treff_client.data.networking.commands;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Lukas on 2/28/2018.
 */

public class AcceptContactRequestTest extends AbstractCommandTest {

    private AcceptContactRequestCommand command;
    private int mockId = 1234;

    @Override
    void initCommand() {
        command = new AcceptContactRequestCommand(mockId, mockToken,
                mockUserRepository);
    }

    @Override
    void onResponseTest() {
        command.onResponse(new AcceptContactRequestCommand.Response());
        verify(mockUserRepository).setIsRequesting(mockId, false);
        verify(mockUserRepository).setIsFriend(mockId, true);
    }

    @Override
    void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        AcceptContactRequestCommand.Request request =
                (AcceptContactRequestCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        // TODO make Enum or something
        assertEquals(request.cmd, "accept-contact-request");
    }
}
