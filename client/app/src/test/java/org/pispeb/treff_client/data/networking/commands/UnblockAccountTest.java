package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of AddPollOptionCommand
 */
public class UnblockAccountTest extends AbstractCommandTest {

    private UnblockAccountCommand command;

    @Override
    public void initCommand() {
        command = new UnblockAccountCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new UnblockAccountCommand.Response());
        verify(mockUserRepository).setIsBlocked(mockId, false);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        UnblockAccountCommand.Request request =
                (UnblockAccountCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "unblock-account");
    }
}
