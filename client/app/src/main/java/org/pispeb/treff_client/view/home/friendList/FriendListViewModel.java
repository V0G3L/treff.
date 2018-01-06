package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel {
    private LiveData<List<User>> friends;
    private final UserRepository userRepo;

    @Inject
    public FriendListViewModel(UserRepository userRepo) {

        this.userRepo = userRepo;


    /*
        //TODO remove
        friends = new MutableLiveData<>();
        List<User> newData = new ArrayList<>();
        newData.add(new User(1, "Max Mustermann", true, false));
        newData.add(new User(2, "Erika Experiment", true, false));
        newData.add(new User(3, "Thorsten Test", true, false));

        friends.getValue().addAll(newData);
    */
    }


    public void init() {
        if (friends == null) {
            friends = userRepo.getFriends();
        }
    }


    public LiveData<List<User>> getFriends() {
        if (friends == null) {
            friends = new MutableLiveData<>();
        }
        return friends;
    }

    //TODO remove mock floating action button behaviour
    public void onAddClick() {
        List<User> newData = friends.getValue();
        friends.getValue().add(new User(5, "Norton Neu", true, false));
    }
}
