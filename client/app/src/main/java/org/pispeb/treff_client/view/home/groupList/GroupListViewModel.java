package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;

import java.util.List;

/**
 * Stores and manages the necessary data for the GroupListFragment
 */

public class GroupListViewModel extends ViewModel implements GroupListAdapter.GroupClickedListener {

    private MutableLiveData<List<UserGroup>> groups;
    private final UserGroupRepository userGroupRepository;

    public GroupListViewModel(UserGroupRepository userGroupRepository) {
        this.groups = new MutableLiveData<>();
        this.userGroupRepository = userGroupRepository;
    }

    public MutableLiveData<List<UserGroup>> getGroups() {
        return groups;
    }

    @Override
    public void onItemClicked(int position, UserGroup group) {
        // TODO display group details
    }
}
