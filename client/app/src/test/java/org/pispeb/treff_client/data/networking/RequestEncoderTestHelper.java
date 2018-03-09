package org.pispeb.treff_client.data.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


/**
 * Testing the RequestEncoders functionality
 */

// mock static methods of Treffpunkt
@RunWith(PowerMockRunner.class)
@PrepareForTest({TreffPunkt.class,
        PreferenceManager.class,
        RequestEncoder.class})
public abstract class RequestEncoderTestHelper {

    protected String mockToken = "SomeRandomTokenFromSP";
    protected String mockUserName = "SomeUsername";
    protected String mockPassword = "SomePassword";

    @Mock
    protected Context mockAppContext;
    @Mock
    protected SharedPreferences mockSharedPref;
    @Mock
    protected ConnectionHandler mockConnectionHandler;
    @Mock
    protected Handler mockHandler;

    @Mock
    protected ChatRepository mockChatRepository;
    @Mock
    protected EventRepository mockEventRepository;
    @Mock
    protected UserRepository mockUserRepository;
    @Mock
    protected UserGroupRepository mockUserGroupRepository;

    protected RequestEncoder testEncoder;

    @Before
    public void prepare() {

        // mock static Context retrieval
        PowerMockito.mockStatic(TreffPunkt.class);
        when(TreffPunkt.getAppContext()).thenReturn(mockAppContext);
        // and shared Preferences
        PowerMockito.mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences
                (mockAppContext)).thenReturn(mockSharedPref);
        // mock SharedPreferences of Context
        when(mockSharedPref
                .getString(eq(mockAppContext.getString(R.string.key_token)),
                        anyString()))
                .thenReturn(mockToken);
        when(mockSharedPref
                .getString(eq(mockAppContext.getString(R.string.key_userName)),
                        anyString()))
                .thenReturn(mockUserName);

        // mock creation of ConnectionHandler
        try {
            whenNew(Handler.class).withAnyArguments().thenReturn(mockHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // init testEncoder
        // spy to mock some methods on the Encoder (most importantly the send
        // to CH method in order to bypass multithreading)
        testEncoder = spy(new RequestEncoder());
        // set repos to handle database calls from commands
        testEncoder.setRepos(mockChatRepository, mockEventRepository,
                mockUserGroupRepository, mockUserRepository
        );

        // skip the backgroundHandler
        doAnswer(invocation -> {
            String message = invocation.getArgumentAt(0, String.class);
            mockConnectionHandler.sendMessage(message);
            return null;
        }).when(testEncoder).sendToCH(anyString());

        // set mock ConnectionHandler for any actions that don't require
        // running in a background thread
        testEncoder.setConnectionHandler(mockConnectionHandler);

        // print any invocations on CH to Console
        doAnswer(invocation -> {
            System.out.println(invocation.getArgumentAt(0, String.class));
            return null;
        }).when(mockConnectionHandler).sendMessage(anyString());

    }
}
