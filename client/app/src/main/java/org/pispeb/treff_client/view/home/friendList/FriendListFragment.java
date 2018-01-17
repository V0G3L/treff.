package org.pispeb.treff_client.view.home.friendList;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FragmentFriendListBinding;
import org.pispeb.treff_client.view.friend.FriendActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Fragment hosting RecyclerView of FriendList and handling onClick-events
 * from items in the List
 */

public class FriendListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // binding to layout
        final FragmentFriendListBinding binding = FragmentFriendListBinding
                .inflate(inflater, container, false);
        // ViewModel which also serves as onClickListener for adapter
        FriendListViewModel vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(FriendListViewModel.class);
        // adapter to display items
        final FriendListAdapter adapter = new FriendListAdapter(vm);

        vm.getFriends().observe(this, friends -> {
            adapter.setData(friends);
        });

        vm.getState().observe(this, state -> callback(state));
        binding.setVm(vm);

        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        return binding.getRoot();
    }

    private void callback(State state) {
        switch (state.call) {
            case DISPLAY_FRIEND_DETAILS:
                FriendActivity.start(this, state.value);
                break;
            case ADD_FRIEND:
                AddFriendActivity.start(this);
                break;
            default:
                Log.i("FriendList", "Illegal VM State");
        }
    }
}
