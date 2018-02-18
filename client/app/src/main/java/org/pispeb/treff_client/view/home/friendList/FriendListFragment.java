package org.pispeb.treff_client.view.home.friendList;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentFriendListBinding;
import org.pispeb.treff_client.view.friend.FriendActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Fragment hosting RecyclerView of FriendList and handling onClick-events
 * from items in the List
 */

public class FriendListFragment extends Fragment {

    private FragmentFriendListBinding binding;
    private FriendListViewModel vm;
    private FriendListAdapter adapter;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // frameBinding to layout
        binding = FragmentFriendListBinding
                .inflate(inflater, container, false);
        // ViewModel which also serves as onClickListener for adapter
        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(FriendListViewModel.class);
        binding.setVm(vm);
        // adapter to display items
        adapter = new FriendListAdapter(vm);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        vm.getFriends().observe(this, friends -> {
            adapter.setList(friends);
        });

        vm.getState().observe(this, state -> callback(state));

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
            case SHOW_PENDING_DIALOG:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.request_pending);
                builder.setPositiveButton(R.string.ok, ((dialog, which) -> {
                    dialog.dismiss();
                }));
                builder.show();
                break;
            case SHOW_REQUESTING_DIALOG:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.user_is_requesting));
                builder.setPositiveButton(R.string.accept, ((dialog, which) -> {
                    vm.accept(userId);
                    dialog.dismiss();
                }));
                builder.setNegativeButton(R.string.decline,
                        ((dialog, which) -> {
                    vm.decline(userId);
                    dialog.dismiss();
                }));
                builder.show();
                break;
            default:
                Log.i("FriendList", "Illegal VM State");
        }
    }
}
