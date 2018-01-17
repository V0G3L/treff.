package org.pispeb.treff_client.view.group;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;

/**
 * Holding Group that is currently displayed in Activity
 */

public class GroupViewModel extends ViewModel {
    private LiveData<UserGroup> group;
    private UserGroupRepository userGroupRepository;

    public GroupViewModel(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public void setGroupById(int groupId) {
        this.group = userGroupRepository.getGroup(groupId);
    }

    public LiveData<UserGroup> getGroup() {
        return group;
    }
}
