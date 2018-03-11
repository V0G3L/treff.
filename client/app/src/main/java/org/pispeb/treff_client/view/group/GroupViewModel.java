package org.pispeb.treff_client.view.group;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.List;

/**
 * Holding Group that is currently displayed in Activity
 */

public class GroupViewModel extends ViewModel
        implements MemberListAdapter.MemberClickedListener {
    // group currently displayed
    private LiveData<UserGroup> group;
    private LiveData<List<User>> members;

    private User lastSelected;

    // repos
    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;

    private SingleLiveEvent<State> state;

    public GroupViewModel(UserGroupRepository userGroupRepository,
                          UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.state = new SingleLiveEvent<>();
    }

    /**
     * set the group to be displayed
     *
     * @param groupId id of that group
     */
    public void setGroupById(int groupId) {
        this.group = userGroupRepository.getGroupLiveData(groupId);
        this.members = userGroupRepository.getGroupMembers(groupId);
    }

    /**
     * display dialog to add new members
     */
    public void onAddMemberClick() {
        state.postValue(new State(ViewCall.SHOW_ADD_MEMBER_DIALOG, 0));
    }

    /**
     * add member given its id
     *
     * @param userId id of user to be added
     */
    public void addMember(int userId) {
        userGroupRepository.requestAddMembersToGroup(
                group.getValue().getGroupId(), userId);
    }

    /**
     * leave the group and delete it locally
     */
    public void leave() {
        state.postValue(new State(ViewCall.LEFT_GROUP, 0));
        userGroupRepository.remove(group.getValue());
    }

    @Override
    public void onItemClicked(int position, User user) {
        lastSelected = user;
        state.setValue(new State(ViewCall.DISPLAY_MEMBERSHIP,
                user.getUserId()));
    }

    public LiveData<UserGroup> getGroup() {
        return group;
    }

    public LiveData<List<User>> getMembers() {
        return members;
    }

    public LiveData<List<User>> getFriends() {
        return userRepository.getFriendsAsList();
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    public User getLastSelected() {
        return lastSelected;
    }

    public void kickUser(int groupId, int... members) {
        if (group.getValue() != null) {
            userGroupRepository.requestKickMembers(groupId, members);
        }
    }

    public void changeGroupName(int groupId, String newName) {
        userGroupRepository.requestNameChange(groupId, newName);
    }
}
