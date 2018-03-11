package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of BlockAccountCommand
 */

public class BlockAccountTest extends AbstractCommandTest {

    private BlockAccountCommand command;

    @Override
    public void initCommand() {
        command = new BlockAccountCommand(mockId, mockToken, mockUserRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new BlockAccountCommand.Response());
        verify(mockUserRepository).setIsBlocked(mockId, true);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        BlockAccountCommand.Request request =
                (BlockAccountCommand.Request) abs;

        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "block-account");
    }
}
