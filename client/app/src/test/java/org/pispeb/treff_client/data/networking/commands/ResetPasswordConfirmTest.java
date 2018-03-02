package org.pispeb.treff_client.data.networking.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Test to check the main function of ResetPasswordConfirmCommand
 */

public class ResetPasswordConfirmTest extends AbstractCommandTest {

    private ResetPasswordConfirmCommand command;
    private String mockResetCode = "Reset Password. NOW!";

    @Override
    public void initCommand() {
        command = new ResetPasswordConfirmCommand(mockResetCode, mockNewPassword);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new ResetPasswordConfirmCommand.Response());
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        ResetPasswordConfirmCommand.Request request =
                (ResetPasswordConfirmCommand.Request) abs;
        assertEquals(request.code, mockResetCode);
        assertEquals(request.newPass, mockNewPassword);
        assertEquals(request.cmd, "reset-password-confirm");
    }
}
