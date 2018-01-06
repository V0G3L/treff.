package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel {
    private LiveData<List<User>> users;
    private UserRepository userRepo;

    public FriendListViewModel(Context context) {

        this.userRepo = UserRepository.getUserRepository(context);
        users = userRepo.getFriends();


        //TODO remove
        users = new MutableLiveData<>();
        List<User> newData = new ArrayList<>();
        newData.add(new User(1, "Max Mustermann", true, false));
        newData.add(new User(2, "Erika Experiment", true, false));
        newData.add(new User(3, "Thorsten Test", true, false));

        users.getValue().addAll(newData);
    }

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
        }
        return users;
    }

    public void onAddClick() {
        List<User> newData = users.getValue();
        users.getValue().add(new User(5, "Norton Neu", true, false));
    }
}
