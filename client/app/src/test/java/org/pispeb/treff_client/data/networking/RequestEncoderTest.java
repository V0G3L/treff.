package org.pispeb.treff_client.data.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
public class RequestEncoderTest {

    private String mockToken = "SomeRandomTokenFromSP";
    private String mockUserName = "SomeUsername";

    @Mock
    private Context mockAppContext;
    @Mock
    private SharedPreferences mockSharedPref;
    @Mock
    private ConnectionHandler mockConnectionHandler;
    @Mock
    private Handler mockHandler;

    private RequestEncoder testEncoder;

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
        testEncoder = spy(RequestEncoder.getInstance());

        // skip the backgroundHandler
        doAnswer(invocation -> {
            String message = invocation.getArgumentAt(0, String.class);
            mockConnectionHandler.sendMessage(message);
            return null;
        }).when(testEncoder).sendToCH(anyString());

        testEncoder.setConnectionHandler(mockConnectionHandler);

        doAnswer(invocation -> {
            System.out.println(invocation.getArgumentAt(0, String.class));
            return null;
        }).when(mockConnectionHandler).sendMessage(anyString());

    }

    @Test
    public void singletonTest() {
        RequestEncoder enc1 = RequestEncoder.getInstance();
        RequestEncoder enc2 = RequestEncoder.getInstance();

        assertTrue(enc1 == enc2);
    }

    @Test
    public void simpleCommandTest() {
        testEncoder.sendChatMessage(1234, "message");
        verify(mockConnectionHandler).sendMessage(anyString());
        PowerMockito.verifyNoMoreInteractions(mockConnectionHandler);
    }

    @Test
    public void commandQueueTest() {
        testEncoder.sendChatMessage(1234, "message1");
        testEncoder.sendChatMessage(1234, "message2");
        verify(mockConnectionHandler).sendMessage(contains("message1"));
        verifyNoMoreInteractions(mockConnectionHandler);
    }

}
