package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.home.friendList.FriendListAdapter;

import java.util.HashSet;
import java.util.Set;


public class AddGroupViewModel extends ViewModel implements
        CheckedFriendListAdapter.FriendClickedListener {
    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;
    private MutableLiveData<Boolean> done;
    private LiveData<PagedList<User>> friends;

    private String groupname;

    private Set<User> selectedUsers;

    public AddGroupViewModel(UserGroupRepository userGroupRepository,
                             UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        friends = userRepository.getFriends();
        groupname = "";
        done = new MutableLiveData<>();
        done.setValue(false);

        this.selectedUsers = new HashSet<>();
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
        if (!groupname.equals("") && !selectedUsers.isEmpty()) {
            // TODO determine actual members to add to group
            int[] members = new int[selectedUsers.size()];
            int cnt = 0;
            for (User u : selectedUsers) {
                members[cnt] = u.getUserId();
            }
            userGroupRepository.requestAddGroup(groupname, 0);
            done.postValue(true);
        }
    }

    @Override
    public void onItemClicked(int position, User user, boolean isChecked) {
        if (isChecked) {
            selectedUsers.add(user);
        } else {
            selectedUsers.remove(user);
        }
    }

    public LiveData<PagedList<User>> getFriends() {
        return friends;
    }

    public MutableLiveData<Boolean> getDone() {
        return done;
    }
}
