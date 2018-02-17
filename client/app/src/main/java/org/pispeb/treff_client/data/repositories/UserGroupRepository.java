package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.view.home.TreffPunkt;

import java.util.List;

import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

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
        // TODO test
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int user = pref.getInt(ctx.getString(R.string.key_userId), -1);

        backgroundHandler.post(() -> {
            //add group
            userGroupDao.save(group);
            //add yourself to the group
            userGroupDao.save(new GroupMembership(user, group.getGroupId()));
        });
    }

    public void remove(UserGroup group) {
        //TODO network calls

        // TODO test
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int user = pref.getInt(ctx.getString(R.string.key_userId), -1);
        encoder.removeGroupMembers(group.getGroupId(), new int[]{user});

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

            userGroupDao.delete(group);
        });
    }

    public void addGroupMembers(int groupId, int[] members) {
        for (int i = 0; i < members.length; i++ ) {
            userGroupDao.save(new GroupMembership(groupId, members[i]));
        }
    }

    public void removeGroupMembers(int groupId, int[] members) {
        for (int i = 0; i < members.length; i++ ) {
            userGroupDao.delete(new GroupMembership(groupId, members[i]));
        }
    }

    public void updateGroup(UserGroup userGroup) {
        userGroupDao.update(userGroup);
    }

    public void requestAddGroup(String groupName, int... members) {
        encoder.createGroup(groupName, members);
    }

    public void requestAddMembersToGroup(int groupId, int... members) {
        encoder.addGroupMembers(groupId, members);
    }
}
