package org.pispeb.treffpunkt.client.view.home.friendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

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
        this.friends = userRepository.getAll();
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

    @Override
    public void onAcceptClicked(User user) {
        userRepository.requestAccept(user.getUserId());
    }

    @Override
    public void onDeclineClicked(User user) {
        userRepository.requestDecline(user.getUserId());
    }

    @Override
    public void onCancelClicked(User user) {
        userRepository.requestCancel(user.getUserId());
    }

    public void unblock(int userID) {
        userRepository.requestIsBlocked(userID, false);
    }

    @Override
    public void onItemClicked(int position, User user) {
        if (user.isBlocked()) {
            state.setValue(new State(ViewCall.SHOW_BLOCKED_DIALOG,
                    user.getUserId()));
        } else if (user.isFriend()) {
            state.setValue(new State(ViewCall.DISPLAY_FRIEND_DETAILS,
                    user.getUserId()));
        }
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
