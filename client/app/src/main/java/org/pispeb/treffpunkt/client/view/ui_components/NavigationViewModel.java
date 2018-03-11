package org.pispeb.treffpunkt.client.view.ui_components;

import android.arch.lifecycle.ViewModel;

import org.pispeb.treffpunkt.client.data.repositories.ChatRepository;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;

/**
 * Created by matth on 10.03.2018.
 */

public class NavigationViewModel extends ViewModel {

    private RepositorySet repos;

    public NavigationViewModel(ChatRepository chatRepository,
                               EventRepository eventRepository,
                               UserGroupRepository userGroupRepository,
                               UserRepository userRepository) {
        this.repos = new RepositorySet(chatRepository, eventRepository, userGroupRepository,
                userRepository);
    }

    protected void wipeAllRepos() {
        repos.chatRepository.deleteAllMessages();
        repos.eventRepository.deleteAllEvents();
        repos.userGroupRepository.deleteAllMemberships();
        repos.userGroupRepository.deleteAllGroups();
        repos.userRepository.deleteAllUsers();
    }
}
