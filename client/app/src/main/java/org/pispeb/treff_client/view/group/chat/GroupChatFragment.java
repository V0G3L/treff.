package org.pispeb.treff_client.view.group.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FragmentGroupChatBinding;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

/**
 * Display chat messages from members of the group
 */

public class GroupChatFragment extends Fragment {

    private GroupChatViewModel vm;
    private FragmentGroupChatBinding binding;
    private GroupChatAdapter adapter;

    private int groupId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupChatBinding
                .inflate(inflater, container, false);
        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(getContext()))
                .get(GroupChatViewModel.class);
        adapter = new GroupChatAdapter();


        //set groupID
        vm.setGroup(groupId);

        vm.getMessages().observe(this, messages -> {
            adapter.setList(messages);
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
            case UPDATE_VIEW:
                binding.text.setText("");
                break;
            default:
                Log.e("Chat", "Illegal VM State");
        }
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
    }
}
