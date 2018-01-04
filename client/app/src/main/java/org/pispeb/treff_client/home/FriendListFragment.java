package org.pispeb.treff_client.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FragmentFriendListBinding;
import org.pispeb.treff_client.entities.User;
import org.pispeb.treff_client.lists.FriendListAdapter;


public class FriendListFragment extends Fragment implements FriendListAdapter.FriendClickedListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentFriendListBinding binding = FragmentFriendListBinding.inflate(inflater, container, false);
        final FriendListAdapter adapter = new FriendListAdapter(this);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.list.setHasFixedSize(true);

        FriendListViewModel vm = ViewModelProviders.of(this).get(FriendListViewModel.class);
        vm.getData().observe(this, data -> {
            adapter.setData(data);
        });
        binding.setVm(vm);

        return binding.getRoot();
    }

    @Override
    public void onItemClicked(int position, User user) {
        //TODO start friend activity
    }
}
