package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of LoginCommand
 */
public class PublishPositionTest extends AbstractCommandTest {

    private PublishPositionCommand command;

    @Override
    public void initCommand() {
        command = new PublishPositionCommand(mockGroupId, mockTimeEnd, mockToken, mockUserGroupRepository);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new PublishPositionCommand.Response());
        verify(mockUserGroupRepository).setIsSharing(mockGroupId, true);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        PublishPositionCommand.Request request =
                (PublishPositionCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.timeEnd, mockTimeEnd);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "publish-position");
    }
}
