package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;

import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;

import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;

public class UserGroupRepository {
    private UserGroupDao userGroupDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public UserGroupRepository(UserGroupDao userGroupDao,
                               RequestEncoder encoder,
                               Handler backgroundHandler) {
        this.userGroupDao = userGroupDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    public LiveData<UserGroup> getGroup(int id) {
        return userGroupDao.getGroupByID(id);
    }

    public LiveData<PagedList<UserGroup>> getGroups() {
        return new LivePagedListBuilder<>(userGroupDao.getAllGroups(), 30)
                .build();
    }

    public LiveData<List<UserGroup>> getGroupsInList() {
        return userGroupDao.getAllGroupsInList();
    }

    public void add(UserGroup group) {
        backgroundHandler.post(() -> {
            userGroupDao.save(group);
        });
    }

    public void remove(UserGroup group) {
        //TODO: network call
        userGroupDao.deleteGroups(group);
    }
}
