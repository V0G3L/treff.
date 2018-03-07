package org.pispeb.treff_client.data.repositories;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pispeb.treff_client.data.entities.GroupMembership;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * TODO doc
 */
public class UserGroupRepositoryTest extends RepositoryTest {

    private UserGroupRepository testUserGroupRepository;
    private int[] testMemberIds = {7654, 8765};

    @Before
    public void prepare() {
        testUserGroupRepository = new UserGroupRepository(mockUserGroupDao,
                mockUserDao, mockEventDao, mockChatDao, mockEncoder,
                mockHandler);
    }

    @Test
    public void addGroupTest() {
        testUserGroupRepository.addGroup(mockGroup);

        verify(mockUserGroupDao).save(mockGroup);
    }

    @Test
    public void addMembersTest() {
        testUserGroupRepository
                .addGroupMembers(mockGroup.getGroupId(), testMemberIds);

        ArgumentCaptor<GroupMembership> gmCaptor = ArgumentCaptor.forClass(GroupMembership.class);
        verify(mockUserGroupDao, times(2)).save(gmCaptor.capture());
        List<GroupMembership> gms = gmCaptor.getAllValues();
        assertTrue(gms.get(0).getGroupId() == mockGroup.getGroupId());
        assertTrue(gms.get(1).getGroupId() == mockGroup.getGroupId());
        assertTrue(gms.get(0).getUserId() == 7654);
        assertTrue(gms.get(1).getUserId() == 8765);
    }

    @Test
    public void getMembersTest() {
        //depends on addMembersTest
        testUserGroupRepository
                .addGroupMembers(mockGroup.getGroupId(), testMemberIds);

        testUserGroupRepository.getGroupMembers(mockGroup.getGroupId());

        verify(mockUserGroupDao, times(1))
                .getGroupMembershipsByGroupIdLiveData(mockGroup.getGroupId());
        verify(mockUserDao, times(testMemberIds.length)).getUserById(anyInt());
    }
}
