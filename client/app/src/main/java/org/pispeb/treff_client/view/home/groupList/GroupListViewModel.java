package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;

import java.util.List;
import java.util.Random;

/**
 * Stores and manages the necessary data for the GroupListFragment
 */

public class GroupListViewModel extends ViewModel implements GroupListAdapter.GroupClickedListener {

    private LiveData<List<UserGroup>> groups;
    private final UserGroupRepository userGroupRepository;

    public GroupListViewModel(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
        this.groups = userGroupRepository.getGroups();
    }

    public LiveData<List<UserGroup>> getGroups() {
        return groups;
    }


    public void onAddClick() {
        //TODO open activity to create new Group
        Log.i("GLVM", "add new group");
        userGroupRepository.add(new UserGroup("Good Group", (int) System.currentTimeMillis()));
    }

    @Override
    public void onItemClicked(int position, UserGroup group) {
        // TODO display group details
    }
}
