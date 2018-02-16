package org.pispeb.treff_client.view.friend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class FriendViewModel extends ViewModel {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    private LiveData<User> user;

    public FriendViewModel(UserGroupRepository userGroupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    public void setUserById(int userId) {
        user = userRepository.getUser(userId);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void onBlockClick() {
        // TODO block user
        userRepository.setIsBlocked(user.getValue().getUserId(), true);
    }

    public void onChatClick() {
        // TODO create new group with the two participants
        userGroupRepository.add(new UserGroup(user.getValue().getUsername(),
                new HashSet<>(), new HashSet<>()));
    }
}
