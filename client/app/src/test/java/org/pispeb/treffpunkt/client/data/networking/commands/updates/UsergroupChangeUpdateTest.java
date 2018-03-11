package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteUsergroup;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by matth on 11.03.2018.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({TreffPunkt.class,
        PreferenceManager.class,
        UsergroupChangeUpdate.class})
public class UsergroupChangeUpdateTest extends AbstractUpdateTest {

    private int ownID = 9876;
    private String idString = "id";
    private int[] ids = {0, 1, 2, 3};
    private int[] ids2 = {0, 1, 2, 3, 9876};
    private CompleteUsergroup completeUsergroup = new CompleteUsergroup("", group1, "Gruppe", ids, null, null);
    private CompleteUsergroup completeUsergroup2 = new CompleteUsergroup("", group1, "Gruppe", ids2, null, null);
    private CompleteUsergroup completeUsergroup3 = new CompleteUsergroup("", group2, "Gruppe", ids2, null, null);

    @Mock
    UserGroup mockUsergroup = mock(UserGroup.class);


    @Before
    public void setUp() {
        // mock static Context retrieval
        PowerMockito.mockStatic(TreffPunkt.class);
        when(TreffPunkt.getAppContext()).thenReturn(mockAppContext);

        PowerMockito.mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences
                (mockAppContext)).thenReturn(mockPreferences);

        try {
            PowerMockito.whenNew(UserGroup.class).withAnyArguments().thenReturn(mockUsergroup);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(mockAppContext.getString(R.string.key_userId)).thenReturn(idString);
        when(mockPreferences.getInt(idString, -1)).thenReturn(ownID);

        when(mockUserGroupRepository.getGroup(group1)).thenReturn(mockUsergroup);
        when(mockUserGroupRepository.getGroup(group2)).thenReturn(null);
    }

    @Test
    public void applyUpdate() throws Exception {

        //Existing group and kicked
        UsergroupChangeUpdate update = new UsergroupChangeUpdate(date, creator1, completeUsergroup);
        update.applyUpdate(mockRepos);
        verify(mockUserGroupRepository).getGroup(group1);
        verify(mockUsergroup).setName("Gruppe");
        verify(mockUserGroupRepository).updateGroupMembers(group1, ids);
        verify(mockUserGroupRepository).removeGroup(mockUsergroup);

    }

    @Test
    public void applUpdate2() {
        //Existing group and not kicked
        UsergroupChangeUpdate update2 = new UsergroupChangeUpdate(date, creator1, completeUsergroup2);
        update2.applyUpdate(mockRepos);
        verify(mockUserGroupRepository).getGroup(group1);
        verify(mockUsergroup).setName("Gruppe");
        verify(mockUserGroupRepository).updateGroupMembers(group1, ids2);
        verify(mockUserGroupRepository).updateGroup(mockUsergroup);
    }

    @Test
    public void applUpdate3() {
        //New group and not kicked
        UsergroupChangeUpdate update3 = new UsergroupChangeUpdate(date, creator1, completeUsergroup3);
        update3.applyUpdate(mockRepos);
        verify(mockUserGroupRepository).getGroup(group2);
        verify(mockUserGroupRepository).updateGroupMembers(group2, ids2);
        verify(mockUserGroupRepository).addGroup(mockUsergroup);
    }

}