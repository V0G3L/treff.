package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel
        implements FriendListAdapter.FriendClickedListener {

    private final LiveData<PagedList<User>> friends;
    private final UserRepository userRepository;

    private SingleLiveEvent<State> state;

    public FriendListViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.friends = userRepository.getFriendsAndPending();
        this.state = new SingleLiveEvent<>();
        this.state.setValue(new State(ViewCall.IDLE, -1));

        // sync list with server
        this.userRepository.requestRefresh();
    }

    public LiveData<PagedList<User>> getFriends() {
        return friends;
    }

    public void onAddClick() {
        state.setValue(new State(ViewCall.ADD_FRIEND, 0));
    }

    public void decline(int userId) {
        userRepository.requestAccept(userId);
    }

    public void accept(int userId) {
        userRepository.requestDecline(userId);
    }

    @Override
    public void onItemClicked(int position, User user) {
        if (user.isRequestPending()) {
            state.setValue(new State(ViewCall.SHOW_PENDING_DIALOG,
                    user.getUserId()));
        } else if (user.isRequesting()) {
            state.setValue(new State(ViewCall.SHOW_REQUESTING_DIALOG,
                    user.getUserId()));
        } else {
            state.setValue(new State(ViewCall.DISPLAY_FRIEND_DETAILS,
                    user.getUserId()));
        }
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
