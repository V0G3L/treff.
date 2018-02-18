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

    public LiveData<User> getUserLiveData(int userID) {
        //TODO send update request to server
        return userDao.getUserByIdLiveData(userID);
    }

    public User getUser(String username) {
        //TODO send update request to server
        return userDao.getUserByName(username);
    }

    public LiveData<PagedList<User>> getFriends() {
        //TODO send update request to server
        encoder.getContactList();
        return new LivePagedListBuilder<>(userDao.getFriends(), 30).build();
    }

    public LiveData<PagedList<User>> getFriendsAndPending() {
        //TODO send update request to server
        encoder.getContactList();
        return new LivePagedListBuilder<>(userDao.getFriendsAndPending(), 30)
                .build();
    }

    public LiveData<List<User>> getFriendsAsList() {
        return userDao.getFriendsAsList();
    }

    public void setIsBlocked(int userId, boolean isBlocked) {
        backgroundHandler.post(() -> {
            userDao.setBlocked(userId, isBlocked);
        });
    }

    public void setIsFriend(int userId, boolean isFriend) {
        backgroundHandler.post(() -> {
            userDao.setIsFriend(userId, isFriend);
        });
    }

    public void setIsPending(int userId, boolean isPending) {
        backgroundHandler.post(() -> {
            userDao.setIsPending(userId, isPending);
        });
    }

    public void setIsRequesting(int userId, boolean isRequesting) {
        backgroundHandler.post(() -> {
            userDao.setIsRequesting(userId, isRequesting);
        });
    }

    public void setUserName(int userId, String username) {
        backgroundHandler.post(() -> {
            userDao.setUserName(userId, username);
        });
    }

    public void addUser(User user) {
        backgroundHandler.post(() -> {
            userDao.save(user);
        });
    }

    public void resetAllUsers() {
        backgroundHandler.post(() -> {
            List<User> users = userDao.getAllAsList();
            for (User u : users) {
                userDao.reset(u.getUserId());
            }
        });
    }

    public void updateUser(User user) {
        backgroundHandler.post(() -> {
            userDao.update(user);
        });
    }

    public void requestAddUser(String username) {
        encoder.sendContactRequest(username);
    }

    public void requestIsBlocked(int userId, boolean isBlocked) {
        if (isBlocked) {
            encoder.blockAccount(userId);
        } else {
            encoder.unblockAccount(userId);
        }
    }

    public void requestAccept(int userId) {
        encoder.acceptContactRequest(userId);
    }

    public void requestDecline(int userId) {
        encoder.cancelContactRequest(userId);
    }
}
