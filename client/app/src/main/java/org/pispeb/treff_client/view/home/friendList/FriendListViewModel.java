package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;

import java.util.List;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel implements FriendListAdapter.FriendClickedListener {

    private LiveData<List<User>> friends;
    private final UserRepository userRepository;

    public FriendListViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.friends = userRepository.getFriends();

        /*
        // TODO revise
        if (friends == null) {
            friends = userRepository.getFriends();
        }
        */


    }

    public LiveData<List<User>> getFriends() {
        return friends;
    }

    //TODO remove mock floating action button behaviour
    public void onAddClick() {
        Log.i("FLVM", "add new friend");
        userRepository.add(new User((int) System.currentTimeMillis(), "Norton Neu", true, false));
    }

    @Override
    public void onItemClicked(int position, User user) {
        //TODO start friend activity
    }
}
