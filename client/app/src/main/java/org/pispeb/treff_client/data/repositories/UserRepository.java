package org.pispeb.treff_client.data.repositories;


import android.arch.lifecycle.LiveData;

import org.pispeb.treff_client.data.database.UserDao;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private UserDao userDao;
    private RequestEncoder encoder;

    @Inject
    public UserRepository(UserDao userDao, RequestEncoder encoder) {
        this.userDao = userDao;
        this.encoder = encoder;
    }

    public LiveData<User> getUser(int userID) {
        //TODO send update request to server
        return userDao.getUserByID(userID);
    }

    public LiveData<List<User>> getFriends() {
        //TODO send update request to server
        return userDao.getFriends();
    }



}
