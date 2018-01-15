package org.pispeb.treff_client.view.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import org.pispeb.treff_client.data.database.EventDao;
import org.pispeb.treff_client.data.database.TreffDatabase;
import org.pispeb.treff_client.data.database.UserDao;
import org.pispeb.treff_client.data.database.UserGroupDao;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.friend.FriendViewModel;
import org.pispeb.treff_client.view.home.eventList.EventListViewModel;
import org.pispeb.treff_client.view.home.friendList.AddFriendActivity;
import org.pispeb.treff_client.view.home.friendList.AddFriendViewModel;
import org.pispeb.treff_client.view.home.friendList.FriendListViewModel;
import org.pispeb.treff_client.view.home.groupList.AddGroupViewModel;
import org.pispeb.treff_client.view.home.groupList.GroupListViewModel;

/**
 * Factory to pass Daos to Repositories
 * and those repos to ViewModels
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory INSTANCE;

    private final UserDao userDao;
    private final UserGroupDao userGroupDao;
    private final EventDao eventDao;

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final EventRepository eventRepository;

    private final RequestEncoder encoder;

    public static ViewModelFactory getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ViewModelFactory(context);
        }
        return INSTANCE;
    }

    private ViewModelFactory(Context context) {
        TreffDatabase database = TreffDatabase.getInstance(context);
        userDao = database.getUserDao();
        userGroupDao = database.getUserGroupDao();
        eventDao = database.getEventDao();

        encoder = new RequestEncoder();

        HandlerThread thread = new HandlerThread("gbt", HandlerThread.MIN_PRIORITY);
        thread.start();
        Handler handler = new Handler(thread.getLooper());

        userRepository = new UserRepository(userDao, encoder, handler);
        userGroupRepository = new UserGroupRepository(userGroupDao, encoder, handler);
        eventRepository = new EventRepository(eventDao, encoder, handler);
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
        } else if (AddGroupViewModel.class.isAssignableFrom(modelClass)) {
            return (T) new AddGroupViewModel(userGroupRepository);
        }

        throw new IllegalArgumentException("Building an instance of the given class " + modelClass + " is not supported.");
    }
}
