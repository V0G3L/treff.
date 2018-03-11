package org.pispeb.treffpunkt.client.data.networking.commands;

import static org.junit.Assert.*;

/**
 * Test to check the main function of WithdrawVoteForOptionCommand
 */

public class WithdrawVoteForOptionTest extends AbstractCommandTest {

    private WithdrawVoteForOptionCommand command;

    @Override
    public void initCommand() {
        command = new WithdrawVoteForOptionCommand(mockGroupId, mockPollId, mockId, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new WithdrawVoteForOptionCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        WithdrawVoteForOptionCommand.Request request =
                (WithdrawVoteForOptionCommand.Request) abs;

        assertEquals(request.groupId, mockGroupId);
        assertEquals(request.id, mockId);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "withdraw-vote-for-option");
    }

}
