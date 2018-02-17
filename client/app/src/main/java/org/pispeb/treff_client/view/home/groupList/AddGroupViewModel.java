package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;

import java.util.HashSet;

public class AddGroupViewModel extends ViewModel {
    private UserGroupRepository userGroupRepository;
    private String groupname;
    MutableLiveData<Boolean> done;

    public AddGroupViewModel(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
        groupname = "";
        done = new MutableLiveData<>();
        done.setValue(false);
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void onCancelClick() {
        done.postValue(true);
    }

    public void onOkClick() {
        if (!groupname.equals("")) {
            // TODO determine actual members to add to group
            userGroupRepository.requestAddGroup(groupname, 0);
            done.postValue(true);
        }
    }

    public MutableLiveData<Boolean> getDone() {
        return done;
    }
}
