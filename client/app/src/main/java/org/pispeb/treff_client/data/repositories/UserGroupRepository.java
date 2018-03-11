package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.database.UserDao;
import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.entities.GroupMembership;
import org.pispeb.treff_client.data.entities.TransformedLiveData;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.view.util.TreffPunkt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UserGroupRepository {
    private UserGroupDao userGroupDao;
    private UserDao userDao;
    private EventDao eventDao;
    private ChatDao chatDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public UserGroupRepository(UserGroupDao userGroupDao,
                               UserDao userDao,
                               EventDao eventDao,
                               ChatDao chatDao,
                               RequestEncoder encoder,
                               Handler backgroundHandler) {
        this.userGroupDao = userGroupDao;
        this.userDao = userDao;
        this.eventDao = eventDao;
        this.chatDao = chatDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    /**
     * receive groups in various formats
     */
    public LiveData<UserGroup> getGroupLiveData(int id) {
        encoder.getGroupDetails(id);
        return userGroupDao.getGroupLiveDataById(id);
    }

    public UserGroup getGroup(int id) {
        return userGroupDao.getGroupById(id);
    }

    public LiveData<PagedList<UserGroup>> getGroups() {
        return new LivePagedListBuilder<>(userGroupDao.getAllGroups(), 30)
                .build();
    }

    public LiveData<List<UserGroup>> getGroupsInList() {
        return userGroupDao.getAllGroupsInList();
    }

    public LiveData<List<User>> getGroupMembers(int groupId) {

        encoder.getGroupDetails(groupId);

        LiveData<List<GroupMembership>> groupMemberships =
                userGroupDao.getGroupMembershipsByGroupIdLiveData(groupId);

        // convert LiveData list of group memberships to LiveData list of users
        LiveData<List<User>> users = new
                TransformedLiveData<List<GroupMembership>, List<User>>(
                        groupMemberships) {

                    @Override
                    protected List<User> transform(
                            List<GroupMembership> gmList) {
                        List<User> users = new ArrayList<>(gmList.size());

                        for (GroupMembership gm : gmList) {
                            int userID = gm.getUserId();
                            User user = userDao.getUserById(userID);

                            // if user unknown, insert placeholder user and
                            // schedule detail query
                            if (user == null) {
                                user = User.getPlaceholderAndScheduleQuery
                                        (userID, userDao);
                            }
                            users.add(user);
                        }
                        return users;
                    }
                };

        return users;
    }

    public void setIsSharing(int groupId, boolean isSharing) {
        backgroundHandler.post(() -> {
            userGroupDao.setIsSharing(groupId, isSharing);
        });
    }

    /**
     * add a group to the local database
     *
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
     *
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
                    userGroupDao
                            .getGroupMembershipsByGroupId(group.getGroupId()));

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
     *
     * @param groupId ids of members to add
     * @param members id of group to add members to
     */
    public void addGroupMembers(int groupId, int[] members) {
        for (int i = 0; i < members.length; i++) {
            GroupMembership gms = new GroupMembership(members[i], groupId);
            userGroupDao.save(gms);
        }
    }

    /**
     * remove members from a group
     *
     * @param groupId given group
     * @param members array of users to remove
     */
    public void removeGroupMembers(int groupId, int[] members) {
        assert backgroundHandler.post(() -> {
            for (int i = 0; i < members.length; i++) {
                userGroupDao.delete(new GroupMembership(members[i], groupId));
            }
        });
    }

    /**
     * Remove all members from group and replace with new ones
     *
     * @param groupId
     * @param newMembers
     */
    public void updateGroupMembers(int groupId, int[] newMembers) {
        userGroupDao.deleteMembershipsOfGroup(groupId);
        addGroupMembers(groupId, newMembers);
    }

    /**
     * replace group in local database with updated one
     *
     * @param userGroup
     */
    public void updateGroup(UserGroup userGroup) {
        userGroupDao.update(userGroup);
    }

    /**
     * update a location or whether or not the user is sharing location
     *
     * @param membership new membership
     */
    public void updateMembership(GroupMembership membership) {
        userGroupDao.update(membership);
    }


    /**
     * request an id from the server to adda newly created group
     *
     * @param groupName name of the group
     * @param members   list of initial members (at least one)
     */
    public void requestAddGroup(String groupName, int... members) {
        encoder.createGroup(groupName, members);
    }

    /**
     * request the server to add members to group
     *
     * @param groupId effected group
     * @param members effected members
     */
    public void requestAddMembersToGroup(int groupId, int... members) {
        encoder.addGroupMembers(groupId, members);
    }

    /**
     * request the server to remove members from group
     *
     * @param groupId id of the group
     * @param members array of member ids to be kicked
     */
    public void requestKickMembers(int groupId, int... members) {
        encoder.removeGroupMembers(groupId, members);
    }

    /**
     * request a list of all groups from the server
     */
    public void requestRefresh() {
        encoder.listGroups();
    }

    /**
     * Deletes every userGroup
     */
    public void deleteAllGroups() {
        backgroundHandler.post(() -> {
            userGroupDao.deleteAllGroups();
        });
    }

    /**
     * Deletes every userGroupMembership
     */
    public void deleteAllMemberships() {
        backgroundHandler.post(() -> {
            userGroupDao.deleteAllMemberships();
        });
    }
}
