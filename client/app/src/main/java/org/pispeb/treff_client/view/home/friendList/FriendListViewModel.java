package org.pispeb.treff_client.view.home.friendList;

import android.app.DialogFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.friend.FriendActivity;

import java.util.List;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel implements FriendListAdapter.FriendClickedListener {

    private LiveData<List<User>> friends;
    private final UserRepository userRepository;
    private Fragment hostFragment;

    public FriendListViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.friends = userRepository.getFriends();
    }

    public LiveData<List<User>> getFriends() {
        return friends;
    }

    public void onAddClick() {
        AddFriendActivity.start(hostFragment);
    }

    @Override
    public void onItemClicked(int position, User user) {
        FriendActivity.start(hostFragment);
    }

    public void setHostFragment(Fragment hostFragment) {
        this.hostFragment = hostFragment;
    }
}
