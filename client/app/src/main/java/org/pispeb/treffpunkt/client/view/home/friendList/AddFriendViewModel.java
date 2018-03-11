package org.pispeb.treffpunkt.client.view.home.friendList;

import android.arch.lifecycle.ViewModel;

import org.pispeb.treffpunkt.client.data.repositories.UserRepository;
import org.pispeb.treffpunkt.client.view.util.SingleLiveEvent;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewCall;

public class AddFriendViewModel extends ViewModel {

    private UserRepository userRepository;
    private String username;
    SingleLiveEvent<State> state;

    public AddFriendViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        username = "";
        this.state = new SingleLiveEvent<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void onCancelClick() {
        state.postValue(new State(ViewCall.CANCEL, 0));
    }

    public void onOkClick() {
        if (!username.equals("")) {
            userRepository.requestAddUser(username);
            state.postValue(new State(ViewCall.SUCCESS, 0));
        }
    }

    public SingleLiveEvent<State> getState() {
        return state;
    }
}
