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
        return new LivePagedListBuilder<>(userDao.getFriends(), 30).build();
    }

    public void add(User user) {
        backgroundHandler.post(() -> {
            userDao.save(user);
        });
    }



}
