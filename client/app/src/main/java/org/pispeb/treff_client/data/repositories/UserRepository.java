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

    public LiveData<User> getUser(int userID) {
        //TODO send update request to server
        return userDao.getUserByID(userID);
    }

    public LiveData<PagedList<User>> getFriends() {
        //TODO send update request to server
        encoder.getContactList();
        return new LivePagedListBuilder<>(userDao.getFriends(), 30).build();
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

    public void addUser(User user) {
        backgroundHandler.post(() -> {
            userDao.save(user);
        });
    }

    public void requestAddUser(String username) {
        encoder.getUserId(username);
    }

    public void requestIsBlocked(int userId, boolean isBlocked) {
        if (isBlocked) {
            encoder.blockAccount(userId);
        } else {
            encoder.unblockAccount(userId);
        }
    }
}
