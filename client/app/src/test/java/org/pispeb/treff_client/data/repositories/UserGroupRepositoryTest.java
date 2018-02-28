package org.pispeb.treff_client.data.repositories;

import org.junit.Before;
import org.junit.Test;
import org.pispeb.treff_client.data.entities.GroupMembership;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * TODO doc
 */

public class UserGroupRepositoryTest extends RepositoryTest {

    private UserGroupRepository testUserGroupRepository;

    @Before
    public void prepare() {
        testUserGroupRepository = new UserGroupRepository(mockUserGroupDao,
                mockEventDao, mockChatDao, mockEncoder, mockHandler);
    }

    @Test
    public void addGroupTest() {
        testUserGroupRepository.addGroup(mockGroup);

        verify(mockUserGroupDao).save(mockGroup);
    }

    @Test
    public void addMembersTest() {
        int[] memberIds = {7654, 8765};
        testUserGroupRepository
                .addGroupMembers(mockGroup.getGroupId(), memberIds);

        for (int i = 0; i < memberIds.length; i++) {
            int id = memberIds[i];
            verify(mockUserGroupDao)
                    .save(eq(new GroupMembership(mockGroup.getGroupId(), id)));
        }
    }
}
