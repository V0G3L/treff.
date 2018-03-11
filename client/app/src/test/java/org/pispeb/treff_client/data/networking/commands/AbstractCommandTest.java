package org.pispeb.treff_client.data.networking.commands;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Lukas on 2/28/2018.
 */

// mock static methods of Treffpunkt
@RunWith(PowerMockRunner.class)
@PrepareForTest({TreffPunkt.class,
        PreferenceManager.class})

public abstract class AbstractCommandTest {
    @Mock
    protected RequestEncoder mockEncoder;
    @Mock
    protected UserRepository mockUserRepository = mock(UserRepository.class);
    @Mock
    protected UserGroupRepository mockUserGroupRepository = mock(UserGroupRepository.class);
    @Mock
    protected EventRepository mockEventRepository = mock(EventRepository.class);
    @Mock
    protected ChatRepository mockChatRepository = mock(ChatRepository.class);
    @Mock
    protected Context mockAppContext;
    @Mock
    protected SharedPreferences mockSharedPref;
    @Mock
    protected SharedPreferences.Editor mockEditor;

    protected String mockToken = "SomeRandomToken";
    protected String mockPassword = "w34k_P455w0rd";
    protected String mockNewPassword = "B3tt3r_P455w0rd";
    protected String mockQuestion = "Why?";
    protected String mockName = "Test Event or whatever";
    protected String mockEmail = "mock@mockmail.com";
    protected String mockMessage = "Hey Ho";
    protected int mockId = 1234567;
    protected int mockGroupId = 987654321;
    protected int mockEvetntId = 55555;
    protected int[] mockUsers = {12345, 13579, 2468, 11111};
    protected int[] mockEvents = {12345, 13579, 2468, 11111};
    protected int[] mockPolls = {12345, 13579, 2468, 11111};
    protected int[] mockIncomingRequests = {888, 999, 777, 666};
    protected int[] mockOutgoingRequests = {123, 456, 810, 937};
    protected int[] mockBlocks = {234};
    protected int mockPollId = 222675;
    protected long mockLatitude = 10;
    protected long mockLongitude = 50;
    protected double mockla = 10.0;
    protected double mocklo = 50.0;
    protected Position mockPosition = new Position(mockla, mocklo);
    protected Location mockLocation = new Location("mock");
    protected boolean mockMultiChoice = true;
    protected Date mockTimeStart = new Date();
    protected Date mockTimeEnd = new Date();

    @Before
    public abstract void initCommand();

    @Before
    public void initPreferences(){
        // mock static Context retrieval
        PowerMockito.mockStatic(TreffPunkt.class);
        when(TreffPunkt.getAppContext()).thenReturn(mockAppContext);

        PowerMockito.mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences
                (mockAppContext)).thenReturn(mockSharedPref);

        //mock resource usage
        when(mockAppContext.getString(R.string.key_token)).thenReturn("token");
        when(mockAppContext.getString(R.string.key_userId)).thenReturn("userId");
        when(mockAppContext.getString(R.string.key_email)).thenReturn("email");
        when(mockAppContext.getString(R.string.key_userName)).thenReturn("userName");

        when(mockSharedPref.edit()).thenReturn(mockEditor);
        when(mockSharedPref.getInt("userId", -1)).thenReturn(mockId);

        when(mockEditor.putString("token", mockToken)).thenReturn(mockEditor);
        when(mockEditor.putInt("userId", mockId)).thenReturn(mockEditor);
        when(mockEditor.putString("email", mockEmail)).thenReturn(mockEditor);
        when(mockEditor.putString("userName", mockName)).thenReturn(mockEditor);
        when(mockEditor.commit()).thenReturn(true);

        mockLocation.setLatitude(mockla);
        mockLocation.setLongitude(mocklo);
    }

    @Test
    public abstract void onResponseTest();

    @Test
    public abstract void getRequestTest();
}
