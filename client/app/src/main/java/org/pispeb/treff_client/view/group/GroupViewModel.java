package org.pispeb.treff_client.view.group;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;


/**
 * Holding Group that is currently displayed in Activity
 */

public class GroupViewModel extends ViewModel
        implements MemberListAdapter. MemberClickedListener{
    private LiveData<UserGroup> group;
    private LiveData<PagedList<User>> members;
    private UserGroupRepository userGroupRepository;

    private SingleLiveEvent<State> state;

    public GroupViewModel(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
        this.state = new SingleLiveEvent<>();
    }

    public void setGroupById(int groupId) {
        this.group = userGroupRepository.getGroup(groupId);
        this.members = userGroupRepository.getGroupMembers(groupId);
    }

    public LiveData<UserGroup> getGroup() {
        return group;
    }

    public LiveData<PagedList<User>> getMembers() {
        return members;
    }

    public void addMember() {
        //TODO add member dialog (?)
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
        //TODO friend details? or dialog like in mockups
        state.setValue(new State(ViewCall.DISPLAY_FRIEND_DETAILS,
                user.getUserId()));
    }

}
