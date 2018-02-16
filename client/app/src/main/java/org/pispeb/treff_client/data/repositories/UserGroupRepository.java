package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;

import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

public class UserGroupRepository {
    private UserGroupDao userGroupDao;
    private EventDao eventDao;
    private ChatDao chatDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public UserGroupRepository(UserGroupDao userGroupDao,
                               EventDao eventDao,
                               ChatDao chatDao,
                               RequestEncoder encoder,
                               Handler backgroundHandler) {
        this.userGroupDao = userGroupDao;
        this.eventDao = eventDao;
        this.chatDao = chatDao;
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
        //TODO network call
        backgroundHandler.post(() -> {
            userGroupDao.save(group);
        });
    }

    public void remove(UserGroup group) {
        //TODO network calls

        backgroundHandler.post(() -> {
            //TODO remove
            userGroupDao.save(new GroupMembership(12, group.getGroupId()));
            userGroupDao.save(new GroupMembership(23, group.getGroupId()));

            //TODO implement properly
            /*
            //delete group memberships
            userGroupDao.deleteGroupMemberships(
                    userGroupDao.getGroupMembershipsByGroupId(group.getGroupId()).getValue());

            //delete group events;
            eventDao.deleteEvents(eventDao.getEventsByIdSet(group.getEvents()).getValue());

            //TODO: delete polls

            //delete chats
            chatDao.deleteMessages(chatDao.getMessageListByGroupId(group.getGroupId()).getValue());

            */

            userGroupDao.deleteGroup(group);
        });
    }
}
