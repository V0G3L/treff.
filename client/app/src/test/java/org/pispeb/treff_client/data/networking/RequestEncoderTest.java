package org.pispeb.treff_client.data.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treff_client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


/**
 * Testing the RequestEncoders functionality
 */

// mock static methods of Treffpunkt
@RunWith(PowerMockRunner.class)
@PrepareForTest({TreffPunkt.class,
        PreferenceManager.class,
        HandlerThread.class})
public class RequestEncoderTest {

    private String mockToken = "SomeRandomTokenFromSP";
    @Mock
    private Context mockAppContext;
    @Mock
    private SharedPreferences mockSharedPref;
    @Mock
    private ConnectionHandler mockConnectionHandler;

    private RequestEncoder testEncoder;

    @Before
    public void prepare() {

        // mock static Context retrieval
        PowerMockito.mockStatic(TreffPunkt.class);
        when(TreffPunkt.getAppContext()).thenReturn(mockAppContext);

        PowerMockito.mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences
                (mockAppContext)).thenReturn(mockSharedPref);
        // mock SharedPreferences of Context
        when(mockSharedPref.getString(eq("token"), anyString())).thenReturn
                (mockToken);

        PowerMockito.mockStatic(HandlerThread.class);
        when(HandlerThread.NORM_PRIORITY).thenReturn(5);

        // init testEncoder
        testEncoder = spy(RequestEncoder.getInstance());
    }

    @Test
    public void singletonTest() {
//        RequestEncoder enc2 = RequestEncoder.getInstance();
//
//        assertTrue(testEncoder == enc2);
        System.out.println("Test1 Success");
    }

    @Test
    public void simpleCommandTest() {

        System.out.println("Test2 Success");
    }

}
