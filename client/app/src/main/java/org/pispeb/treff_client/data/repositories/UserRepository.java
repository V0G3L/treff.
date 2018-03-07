package org.pispeb.treff_client.data.repositories;


import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;

import org.pispeb.treff_client.data.database.UserDao;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public UserRepository(UserDao userDao, RequestEncoder encoder, Handler backgroundHandler) {
        this.userDao = userDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    /**
     * get user object from local database in different formats
     * @param userID
     * @return
     */
    public LiveData<User> getUserLiveData(int userID) {
        encoder.getUserDetails(userID);
        return userDao.getUserByIdLiveData(userID);
    }

    public User getUser(String username) {
        return userDao.getUserByName(username);
    }

    public LiveData<PagedList<User>> getFriends() {
        encoder.getContactList();
        return new LivePagedListBuilder<>(userDao.getFriends(), 30).build();
    }

    public LiveData<PagedList<User>> getFriendsAndPending() {
        encoder.getContactList();
        return new LivePagedListBuilder<>(userDao.getFriendsAndPending(), 30)
                .build();
    }

    public LiveData<List<User>> getFriendsAsList() {
        return userDao.getFriendsAsList();
    }

    /**
     * set whether user is blocked
     */
    public void setIsBlocked(int userId, boolean isBlocked) {
        backgroundHandler.post(() -> {
            userDao.setBlocked(userId, isBlocked);
        });
    }

    /**
     * set whether user is in contact list
     */
    public void setIsFriend(int userId, boolean isFriend) {
        backgroundHandler.post(() -> {
            userDao.setIsFriend(userId, isFriend);
        });
    }

    /**
     * set whether a contact request to that user is pending
     */
    public void setIsPending(int userId, boolean isPending) {
        backgroundHandler.post(() -> {
            userDao.setIsPending(userId, isPending);
        });
    }

    /**
     * set whether user is requesting to be contacts
     */
    public void setIsRequesting(int userId, boolean isRequesting) {
        backgroundHandler.post(() -> {
            userDao.setIsRequesting(userId, isRequesting);
        });
    }

    /**
     * change he username of the given user
     */
    public void setUserName(int userId, String username) {
        backgroundHandler.post(() -> {
            userDao.setUserName(userId, username);
        });
    }

    /**
     * add a user to the local database
     */
    public void addUser(User user) {
        backgroundHandler.post(() -> {
            userDao.save(user);
        });
    }

    /**
     * reset information about contacts and requests
     */
    public void resetAllUsers() {
        backgroundHandler.post(() -> {
            List<User> users = userDao.getAllAsList();
            for (User u : users) {
                userDao.reset(u.getUserId());
            }
        });
    }

    /**
     * replace user with updated one
     * @param user new user
     */
    public void updateUser(User user) {
        backgroundHandler.post(() -> {
            userDao.update(user);
        });
    }

    /**
     * send a contact request to the user given its
     * @param username username
     */
    public void requestAddUser(String username) {
        encoder.sendContactRequest(username);
    }

    /**
     * request to unblock/block a user
     * @param userId effected user
     * @param isBlocked whether to block or unblock
     */
    public void requestIsBlocked(int userId, boolean isBlocked) {
        if (isBlocked) {
            encoder.blockAccount(userId);
        } else {
            encoder.unblockAccount(userId);
        }
    }

    /**
     * accept a contact request from the given user
     * @param userId id of that user
     */
    public void requestAccept(int userId) {
        encoder.acceptContactRequest(userId);
    }

    /**
     * decline a contact request from the given user
     * @param userId id of that user
     */
    public void requestDecline(int userId) {
        encoder.rejectContactRequest(userId);
    }

    /**
     * sync data with server
     */
    public void requestRefresh() {
        encoder.getContactList();
    }
}
