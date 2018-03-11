package org.pispeb.treffpunkt.client.view.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import org.pispeb.treffpunkt.client.data.database.*;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.data.repositories.*;
import org.pispeb.treffpunkt.client.view.friend.FriendViewModel;
import org.pispeb.treffpunkt.client.view.group.GroupViewModel;
import org.pispeb.treffpunkt.client.view.group.chat.GroupChatViewModel;
import org.pispeb.treffpunkt.client.view.group.eventList.AddEventViewModel;
import org.pispeb.treffpunkt.client.view.group.eventList.GroupEventListViewModel;
import org.pispeb.treffpunkt.client.view.home.eventList.EventListViewModel;
import org.pispeb.treffpunkt.client.view.home.friendList.AddFriendViewModel;
import org.pispeb.treffpunkt.client.view.home.friendList.FriendListViewModel;
import org.pispeb.treffpunkt.client.view.home.groupList.AddGroupViewModel;
import org.pispeb.treffpunkt.client.view.home.groupList.GroupListViewModel;
import org.pispeb.treffpunkt.client.view.home.map.MapViewModel;
import org.pispeb.treffpunkt.client.view.login.LoginViewModel;
import org.pispeb.treffpunkt.client.view.login.RegisterViewModel;
import org.pispeb.treffpunkt.client.view.profile.ProfileViewModel;
import org.pispeb.treffpunkt.client.view.ui_components.NavigationViewModel;

/**
 * Factory to pass Daos to Repositories
 * and those repos to ViewModels
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory INSTANCE;

    private final UserDao userDao;
    private final UserGroupDao userGroupDao;
    private final EventDao eventDao;
    private final ChatDao chatDao;

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final EventRepository eventRepository;
    private final ChatRepository chatRepository;

    private final RequestEncoder encoder;

    public static ViewModelFactory getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ViewModelFactory(context);
        }
        return INSTANCE;
    }

    private ViewModelFactory(Context context) {
        Log.i("ViewmodelFactory", "create");
        TreffDatabase database = TreffDatabase.getInstance(context);
        userDao = database.getUserDao();
        userGroupDao = database.getUserGroupDao();
        eventDao = database.getEventDao();
        chatDao = database.getChatDao();


        encoder = RequestEncoder.getInstance();

        HandlerThread thread = new HandlerThread("gbt",
                HandlerThread.MIN_PRIORITY);
        thread.start();
        Handler handler = new Handler(thread.getLooper());

        userRepository = new UserRepository(userDao, encoder, handler);
        userGroupRepository = new UserGroupRepository(userGroupDao, userDao, eventDao, chatDao, encoder, handler);
        eventRepository = new EventRepository(eventDao, encoder, handler);
        chatRepository = new ChatRepository(chatDao, encoder, handler);

        encoder.setRepos(chatRepository,
                eventRepository,
                userGroupRepository,
                userRepository
        );
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (FriendListViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new FriendListViewModel(userRepository);
        } else if (GroupListViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new GroupListViewModel(userGroupRepository);
        } else if (EventListViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new EventListViewModel(eventRepository);
        } else if (AddFriendViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new AddFriendViewModel(userRepository);
        } else if (FriendViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new FriendViewModel(userGroupRepository, userRepository);
        } else if (MapViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new MapViewModel(
                    userGroupRepository,
                    userRepository,
                    eventRepository);
        } else if (AddGroupViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new AddGroupViewModel(userGroupRepository, userRepository);
        } else if (GroupChatViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new GroupChatViewModel(chatRepository);
        } else if (GroupViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new GroupViewModel(userGroupRepository, userRepository);
        } else if (GroupEventListViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new GroupEventListViewModel(eventRepository);
        } else if (LoginViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new LoginViewModel(encoder);
        } else if (AddEventViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new AddEventViewModel(eventRepository,
                    userGroupRepository);
        } else if (RegisterViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new RegisterViewModel(encoder);
        } else if (ProfileViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new ProfileViewModel(encoder);
        } else if (NavigationViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new NavigationViewModel(
                    chatRepository,
                    eventRepository,
                    userGroupRepository,
                    userRepository);
        }

        throw new IllegalArgumentException(
                "Building an instance of the given class " + modelClass + " " +
                        "is not supported.");
    }

    /**
     * Closes the {@link RequestEncoder}s connection
     */
    public static void closeConnection() {
        if (INSTANCE != null) {
            if (INSTANCE.encoder != null) {
                INSTANCE.encoder.closeConnection();
            }
        }
    }
}
