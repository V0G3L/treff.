package org.pispeb.treffpunkt.client.data.repositories;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * TODO doc
 */

public class UserRepositoryTest extends RepositoryTest {

    private UserRepository testUserRepository;

    @Before
    public void prepare() {
        testUserRepository = new UserRepository(mockUserDao, mockEncoder,
                mockHandler);
    }

    @Test
    public void getUserTest() {
        when(mockUserDao.getUserByName(mockUser.getUsername())).thenReturn
                (mockUser);

        assertEquals(testUserRepository.getUser(mockUser.getUsername()),
                mockUser);
    }

    @Test
    public void blockUserTest() {
        testUserRepository.setIsBlocked(mockUser.getUserId(), true);
        testUserRepository.setIsBlocked(mockUser.getUserId(), false);

        verify(mockUserDao).setBlocked(mockUser.getUserId(), true);
        verify(mockUserDao).setBlocked(mockUser.getUserId(), false);
    }

    @Test
    public void friendUserTest() {
        testUserRepository.setIsFriend(mockUser.getUserId(), true);
        testUserRepository.setIsFriend(mockUser.getUserId(), false);

        verify(mockUserDao).setIsFriend(mockUser.getUserId(), true);
        verify(mockUserDao).setIsFriend(mockUser.getUserId(), false);
    }

    @Test
    public void pendingUserTest() {
        testUserRepository.setIsPending(mockUser.getUserId(), true);
        testUserRepository.setIsPending(mockUser.getUserId(), false);

        verify(mockUserDao).setIsPending(mockUser.getUserId(), true);
        verify(mockUserDao).setIsPending(mockUser.getUserId(), false);
    }

    @Test
    public void requestUserTest() {
        testUserRepository.setIsRequesting(mockUser.getUserId(), true);
        testUserRepository.setIsRequesting(mockUser.getUserId(), false);

        verify(mockUserDao).setIsRequesting(mockUser.getUserId(), true);
        verify(mockUserDao).setIsRequesting(mockUser.getUserId(), false);
    }

    @Test
    public void setUsernameTest() {
        testUserRepository.setUserName(mockUser.getUserId(), "New Username");

        verify(mockUserDao).setUserName(mockUser.getUserId(), "New Username");
    }

    @Test
    public void addUserTest() {
        testUserRepository.addUser(mockUser);

        verify(mockUserDao).save(mockUser);
    }

    // TODO add tests for missing methods

}
