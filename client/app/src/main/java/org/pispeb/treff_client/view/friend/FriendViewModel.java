package org.pispeb.treff_client.view.friend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;

/**
 * {@link ViewModel} providing a single other {@link User}s data to the UI
 */
public class FriendViewModel extends ViewModel {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    private LiveData<User> user;

    public FriendViewModel(UserGroupRepository userGroupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    public void setUserById(int userId) {
        user = userRepository.getUserLiveData(userId);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void onBlockClick() {
        userRepository.requestIsBlocked(user.getValue().getUserId(), true);
    }

    public void onChatClick() {
        userGroupRepository.requestAddGroup(user.getValue().getUsername(), user
                .getValue().getUserId());
    }
}
