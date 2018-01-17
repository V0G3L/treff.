package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.List;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel
        implements FriendListAdapter.FriendClickedListener {

    private LiveData<List<User>> friends;
    private final UserRepository userRepository;

    private SingleLiveEvent<State> state;

    public FriendListViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.friends = userRepository.getFriends();
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, -1));
    }

    public LiveData<List<User>> getFriends() {
        return friends;
    }

    public void onAddClick() {
        state.setValue(new State(ViewCall.ADD_FRIEND, 0));
    }

    @Override
    public void onItemClicked(int position, User user) {
        state.setValue(new State(ViewCall.DISPLAY_FRIEND_DETAILS,
                user.getUserID()));
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
