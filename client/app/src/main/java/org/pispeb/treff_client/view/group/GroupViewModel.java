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
        implements MemberListAdapter. MemberClickedListener{
    private LiveData<UserGroup> group;
    private LiveData<List<User>> members;

    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;

    private SingleLiveEvent<State> state;

    public GroupViewModel(UserGroupRepository userGroupRepository,
                          UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.state = new SingleLiveEvent<>();
    }

    public void setGroupById(int groupId) {
        this.group = userGroupRepository.getGroup(groupId);
        this.members = userGroupRepository.getGroupMembers(groupId);
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

    public void onAddMemberClick() {
        state.postValue(new State(ViewCall.SHOW_ADD_MEMBER_DIALOG, 0));
    }

    public void addMember(int userId) {
        userGroupRepository.requestAddMembersToGroup(
                group.getValue().getGroupId(), userId);
    }

    public void leave() {
        state.postValue(new State(ViewCall.LEFT_GROUP, 0));
        userGroupRepository.remove(group.getValue());
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }

    @Override
    public void onItemClicked(int position, User user) {
        state.setValue(new State(ViewCall.DISPLAY_FRIEND_DETAILS,
                user.getUserId()));
    }

}
