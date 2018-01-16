package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.view.group.GroupActivity;

import java.util.List;
import java.util.Random;

/**
 * Stores and manages the necessary data for the GroupListFragment
 */

public class GroupListViewModel extends ViewModel implements GroupListAdapter.GroupClickedListener {

    private LiveData<List<UserGroup>> groups;
    private final UserGroupRepository userGroupRepository;
    private Fragment hostFragment;

    public GroupListViewModel(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
        this.groups = userGroupRepository.getGroups();
    }

    public LiveData<List<UserGroup>> getGroups() {
        return groups;
    }


    public void onAddClick() {
        AddGroupActivity.start(hostFragment);
    }

    @Override
    public void onItemClicked(int position, UserGroup group) {
        // TODO display group details
        GroupActivity.start(hostFragment);
    }

    public void setHostFragment(Fragment hostFragment) {
        this.hostFragment = hostFragment;
    }
}
