package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.repositories.UserRepository;

public class AddFriendViewModel extends ViewModel {

    private UserRepository userRepository;
    private String username;
    MutableLiveData<Boolean> done;

    public AddFriendViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        username = "";
        done = new MutableLiveData<>();
        done.setValue(false);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void onCancelClick() {
        done.postValue(true);
    }

    public void onOkClick() {
        if (!username.equals("")) {
            userRepository.requestAddUser(username);
            done.postValue(true);
        }
    }

    public MutableLiveData<Boolean> getDone() {
        return done;
    }
}
