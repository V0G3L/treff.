package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;

import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;
import android.os.Handler;

import javax.inject.Singleton;

@Singleton
public class UserGroupRepository {
    private UserGroupDao userGroupDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public UserGroupRepository(UserGroupDao userGroupDao, RequestEncoder encoder, Handler backgroundHandler) {
        this.userGroupDao = userGroupDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    public LiveData<UserGroup> getGroupById (int id) {
        return userGroupDao.getGroupByID(id);
    }

    public LiveData<List<UserGroup>> getGroups () {
        return userGroupDao.getGroups();
    }

    public void add (UserGroup group) {
        backgroundHandler.post(() -> {
            userGroupDao.save(group);
        });
    }
}
