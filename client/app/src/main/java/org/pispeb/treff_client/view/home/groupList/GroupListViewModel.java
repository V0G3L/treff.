package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.List;

/**
 * Stores and manages the necessary data for the GroupListFragment
 */

public class GroupListViewModel extends ViewModel
        implements GroupListAdapter.GroupClickedListener {

    private LiveData<PagedList<UserGroup>> groups;
    private final UserGroupRepository userGroupRepository;

    private SingleLiveEvent<State> state;

    public GroupListViewModel(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
        this.groups = userGroupRepository.getGroups();
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, -1));
    }

    public LiveData<PagedList<UserGroup>> getGroups() {
        return groups;
    }


    public void onAddClick() {
        state.setValue(new State(ViewCall.ADD_GROUP, 0));
    }

    @Override
    public void onItemClicked(int position, UserGroup group) {
        state.setValue(new State(ViewCall.DISPLAY_GROUP_DETAILS,
                group.getGroupId()));
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
