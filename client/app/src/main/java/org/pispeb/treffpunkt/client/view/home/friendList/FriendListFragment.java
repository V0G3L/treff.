package org.pispeb.treffpunkt.client.view.home.friendList;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.FragmentFriendListBinding;
import org.pispeb.treffpunkt.client.view.friend.FriendActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Fragment hosting RecyclerView of FriendList and handling onClick-events
 * from items in the List
 */

public class FriendListFragment extends Fragment {

    private FragmentFriendListBinding binding;
    private FriendListViewModel vm;
    private FriendListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel which also serves as onClickListener for adapter
        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(FriendListViewModel.class);

        // adapter to display items
        adapter = new FriendListAdapter(vm);

        // update adapter when needed
        vm.getFriends().observe(this, friends -> {
            adapter.setList(friends);
        });

        // react to vm callbacks
        vm.getState().observe(this, state -> callback(state));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // frameBinding to layout
        binding = FragmentFriendListBinding
                .inflate(inflater, container, false);

        binding.setVm(vm);

        //configure adapter
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        // item divider
        binding.list.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return binding.getRoot();
    }

    private void callback(State state) {
        AlertDialog.Builder builder;
        int userId = state.value;
        switch (state.call) {
            case ADD_FRIEND:
                Intent addFriendIntent = new Intent(getContext(),
                        AddFriendActivity.class);
                startActivity(addFriendIntent);
                break;
            case DISPLAY_FRIEND_DETAILS:
                Intent friendIntent = new Intent(getContext(), FriendActivity.class);
                friendIntent.putExtra(FriendActivity.USER_INTENT, userId);
                startActivity(friendIntent);
                break;
            case SHOW_BLOCKED_DIALOG:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.blocked);
                builder.setPositiveButton(R.string.unblock, ((dialog, which) -> {
                    vm.unblock(userId);
                    dialog.dismiss();
                }));
                builder.show();
                break;
            default:
                Log.i("FriendList", "Illegal VM State");
        }
    }
}
