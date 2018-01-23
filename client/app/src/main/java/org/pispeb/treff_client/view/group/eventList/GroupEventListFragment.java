package org.pispeb.treff_client.view.group.eventList;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.Occasion;
import org.pispeb.treff_client.databinding.FragmentGroupEventsBinding;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewModelFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Display events and polls created in the group in a list
 */

public class GroupEventListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate layout
        final FragmentGroupEventsBinding binding = FragmentGroupEventsBinding
                .inflate(inflater, container, false);
        GroupEventListViewModel vm = ViewModelProviders.of(this,
                ViewModelFactory.getInstance(getContext())).get
                (GroupEventListViewModel.class);
        final GroupEventListAdapter adapter = new GroupEventListAdapter();

        binding.setVm(vm);

        vm.getState().observe(this, state -> callback(state));

        // merge event and poll list when either of them changes
        vm.getEvents().observe(this, events -> {
            List<Occasion> list = new LinkedList<>();
            if (events != null) {
                list.addAll(events);
            }
            if (vm.getPolls().getValue() != null) {
                list.addAll(vm.getPolls().getValue());
            }
            adapter.setData(list);
        });

        vm.getPolls().observe(this, polls -> {
            List<Occasion> list = new LinkedList<>();
            if (polls != null) {
                list.addAll(polls);
            }
            if (vm.getEvents().getValue() != null) {
                list.addAll(vm.getEvents().getValue());
            }
            adapter.setData(list);
        });

        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        return binding.getRoot();
    }

    // handling callbacks from the ViewModel that require a context
    private void callback(State state) {
        switch (state.call) {
            case DISPLAY_EOP_DIALOG:
                DialogFragment dialog = new EoPDialogFragment();
                dialog.show(getFragmentManager(), "event or poll");
                break;
            default:
                throw new IllegalArgumentException("Illegal VM state");
        }
    }


}
