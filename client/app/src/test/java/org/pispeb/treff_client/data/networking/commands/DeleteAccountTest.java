package org.pispeb.treff_client.data.networking.commands;

import android.content.Intent;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treff_client.view.login.LoginActivity;
import org.pispeb.treff_client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test to check the main function of DeleteAccountCommand
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({DeleteAccountCommand.class})

public class DeleteAccountTest extends AbstractCommandTest {

    private DeleteAccountCommand command;

    @Mock
    private Intent mockIntent = mock(Intent.class);

    @Before
    public void initIntent () {

        try {
            PowerMockito.whenNew(Intent.class).withArguments(mockAppContext, LoginActivity.class).thenReturn(mockIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(mockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)).thenReturn(mockIntent);
    }

    @Override
    public void initCommand() {
        command = new DeleteAccountCommand(mockPassword, mockToken);
    }

    @Override
    public void onResponseTest() {
        command.onResponse(new DeleteAccountCommand.Response());
        verify(mockIntent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        verify(mockAppContext).startActivity(mockIntent);
    }

    @Override
    public void getRequestTest() {
        AbstractRequest abs = command.getRequest();
        DeleteAccountCommand.Request request =
                (DeleteAccountCommand.Request) abs;

        assertEquals(request.pass, mockPassword);
        assertEquals(request.token, mockToken);
        assertEquals(request.cmd, "delete-account");
    }
}
