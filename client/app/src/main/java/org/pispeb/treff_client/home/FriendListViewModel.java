package org.pispeb.treff_client.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the necessary data for the {@link FriendListFragment}
 */

public class FriendListViewModel extends ViewModel {
    private MutableLiveData<List<User>> data;

    public FriendListViewModel() {
        data = new MutableLiveData<>();
        List<User> newData = new ArrayList<>();
        newData.add(new User(1, "Max Mustermann"));
        newData.add(new User(2, "Erika Experiment"));
        newData.add(new User(3, "Thorsten Test"));
        data.postValue(newData);
    }

    public MutableLiveData<List<User>> getData() {
        if(data == null) data = new MutableLiveData<>();
        return data;
    }

    public void onAddClick() {
        List<User> newData = data.getValue();
        newData.add(new User(5, "Norton Neu"));
        data.postValue(newData);
    }
}
