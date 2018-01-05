package org.pispeb.treff_client.repositories;


import android.arch.lifecycle.LiveData;
import android.content.Context;

import org.pispeb.treff_client.database.UserDao;
import org.pispeb.treff_client.database.treffDatabase;
import org.pispeb.treff_client.entities.User;

import java.util.List;

public class UserRepository {
    //TODO private something that creates requests and sends them to the connectionhandler
    private static treffDatabase db;
    private static UserDao userDao;

    private static UserRepository INSTANCE;

    //TODO get proper context, maybe via dagger
    public static UserRepository getUserRepository(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
            INSTANCE.db = treffDatabase.getAppDatabase(context);
            userDao = db.getUserDao();
        }
        return INSTANCE;
    }

    public LiveData<User> getUser(int userID) {
        //TODO send update request to server
        userDao.getUserByID(userID);
        return null;
    }

    public LiveData<List<User>> getFriends() {
        //TODO send update request to server
        userDao.getFriends();
        return null;
    }



}
