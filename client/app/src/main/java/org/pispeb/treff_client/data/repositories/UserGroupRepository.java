package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;

import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.User;
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
        //TODO network call
        return new LivePagedListBuilder<>(userGroupDao.getAllGroups(), 30)
                .build();
    }

    public LiveData<List<UserGroup>> getGroupsInList() {
        //TODO network call
        return userGroupDao.getAllGroupsInList();
    }

    public LiveData<PagedList<User>> getGroupMembers(int groupId) {
        //TODO network call
        return new LivePagedListBuilder<>(userGroupDao.getUsersByGroup(groupId), 30)
                .build();
    }

    public void add(UserGroup group) {
        //TODO network call
        backgroundHandler.post(() -> {
            //add group
            userGroupDao.save(group);
            //add yourself to the group
            //TODO set own user ID
            userGroupDao.save(new GroupMembership(3, group.getGroupId()));
        });
    }

    public void remove(UserGroup group) {
        //TODO network calls

        backgroundHandler.post(() -> {

            //delete group memberships
            userGroupDao.deleteGroupMemberships(
                    userGroupDao.getGroupMembershipsByGroupId(group.getGroupId()));

            //delete group events;
            eventDao.deleteEvents(
                    eventDao.getEventsByIdSet(group.getEvents()));

            //TODO: delete polls

            //delete chats
            chatDao.deleteMessages(
                    chatDao.getMessageListByGroupId(group.getGroupId()));

            userGroupDao.deleteGroup(group);
        });
    }
}
