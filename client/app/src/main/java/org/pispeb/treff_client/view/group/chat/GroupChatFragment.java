package org.pispeb.treff_client.view.group.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FragmentGroupChatBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Display chat messages from members of the group
 */

public class GroupChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentGroupChatBinding binding = FragmentGroupChatBinding
                .inflate(inflater, container, false);
        GroupChatViewModel vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(GroupChatViewModel.class);
        final GroupChatAdapter adapter = new GroupChatAdapter();

        vm.getMessages().observe(this, messages -> {
            adapter.setData(messages);
        });

        binding.setVm(vm);

        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        return binding.getRoot();
    }
}
