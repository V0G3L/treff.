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
import org.pispeb.treff_client.view.util.TreffPunkt;

import java.util.List;

import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
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

    /**
     * receive groups in various formats
     */
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

    public LiveData<PagedList<User>> getGroupMembers(int groupId) {
        encoder.getGroupDetails(groupId);
        return new LivePagedListBuilder<>(userGroupDao.getUsersByGroup(groupId), 30)
                .build();
    }

    public void setIsSharing (int groupId, boolean isSharing) {
        backgroundHandler.post(() -> {
            userGroupDao.setIsSharing(groupId, isSharing);
        });
    }

    /**
     * add a group to the local database
     * @param group group to be added
     */
    public void addGroup(UserGroup group) {
        backgroundHandler.post(() -> {
            //add group
            userGroupDao.save(group);
        });
    }

    /**
     * Leave a group, delete it locally and propagate through all its events
     * and Members
     * @param group group which the user left
     */
    public void remove(UserGroup group) {
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
                    eventDao.getEventsFromGroupInList(group.getGroupId()));

            //TODO: delete polls

            //delete chats
            chatDao.deleteMessages(
                    chatDao.getMessageListByGroupId(group.getGroupId()));

            userGroupDao.delete(group);
        });
    }

    /**
     * Add new Members to a group
     * @param groupId ids of members to add
     * @param members id of group to add members to
     */
    public void addGroupMembers(int groupId, int[] members) {
        for (int i = 0; i < members.length; i++ ) {
            GroupMembership gms = new GroupMembership(members[i], groupId);
//            Log.i("AddGroupMember",
//                    "group: " + gms.getGroupId() + " member: " + gms.getUserId());
            userGroupDao.save(gms);
        }
    }

    /**
     * remove members from a group
     * @param groupId given group
     * @param members array of users to remove
     */
    public void removeGroupMembers(int groupId, int[] members) {
        for (int i = 0; i < members.length; i++ ) {
            userGroupDao.delete(new GroupMembership(members[i], groupId));
        }
    }

    /**
     * replace group in local database with updated one
     * @param userGroup
     */
    public void updateGroup(UserGroup userGroup) {
        userGroupDao.update(userGroup);
    }

    /**
     * request an id from the server to adda newly created group
     * @param groupName name of the group
     * @param members list of initial members (at least one)
     */
    public void requestAddGroup(String groupName, int... members) {
        encoder.createGroup(groupName, members);
    }

    /**
     * request the server to remove members from group
     * @param groupId effected group
     * @param members effected members
     */
    public void requestAddMembersToGroup(int groupId, int... members) {
        encoder.addGroupMembers(groupId, members);
    }
}
