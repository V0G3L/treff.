package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of GetContactListCommand
 */

public class GetContactListTest extends AbstractCommandTest {

    private GetContactListCommand command;

    @Override
    public void initCommand() {
        command = new GetContactListCommand(mockToken, mockUserRepository,
                mockEncoder);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new GetContactListCommand.Response(
                mockUsers, mockIncomingRequests, mockOutgoingRequests,
                mockBlocks));
        verify(mockUserRepository).resetAllUsers();
        for (int c : mockUsers) {
            mockUserRepository.setIsFriend(c, true);
        }
        for (int in : mockIncomingRequests) {
            mockUserRepository.setIsRequesting(in, true);
        }
        for (int out : mockOutgoingRequests) {
            mockUserRepository.setIsPending(out, true);
        }
        for (int b : mockBlocks) {
            mockUserRepository.setIsBlocked(b, true);
        }
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        GetContactListCommand.Request request =
                (GetContactListCommand.Request) abs;

        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "get-contact-list");
    }
}
