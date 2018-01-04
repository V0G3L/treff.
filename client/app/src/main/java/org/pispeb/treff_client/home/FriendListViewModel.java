package org.pispeb.treff_client.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

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
        newData.add(new User("Max Mustermann", "m.m@gmail.com"));
        newData.add(new User("Erika Experiment", "e.e@gmail.com"));
        newData.add(new User("Thorsten Test", "t.t@gmail.com"));
        data.postValue(newData);
    }

    public MutableLiveData<List<User>> getData() {
        if(data == null) data = new MutableLiveData<>();
        return data;
    }

    public void onAddClick() {
        List<User> newData = data.getValue();
        newData.add(new User("Norton Neu", "n.n@gmail.com"));
        data.postValue(newData);
    }
}
