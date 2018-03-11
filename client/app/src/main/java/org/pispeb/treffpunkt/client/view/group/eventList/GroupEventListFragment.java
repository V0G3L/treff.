package org.pispeb.treffpunkt.client.view.group.eventList;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.databinding.FragmentGroupEventsBinding;
import org.pispeb.treffpunkt.client.view.group.GroupActivity;
import org.pispeb.treffpunkt.client.view.home.eventList.EventListAdapter;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Display events and polls created in the group in a list
 */

public class GroupEventListFragment extends Fragment {

    private GroupEventListViewModel vm;
    private int groupId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate layout
        final FragmentGroupEventsBinding binding = FragmentGroupEventsBinding
                .inflate(inflater, container, false);
        vm = ViewModelProviders.of(this,
                ViewModelFactory.getInstance(getContext())).get
                (GroupEventListViewModel.class);
        final EventListAdapter adapter = new EventListAdapter(vm);

        binding.setVm(vm);

        vm.setGroup(groupId);

        vm.getState().observe(this, this::callback);

        // update event list
        vm.getEvents().observe(this, events -> adapter.setList(events));


        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        return binding.getRoot();
    }

    // handling callbacks from the ViewModel that require a context
    private void callback(State state) {
        switch (state.call) {
            case ADD_EVENT:
                //TODO make pretty
                ((GroupActivity)getActivity()).onEventClick();
                break;
            default:
                throw new IllegalArgumentException("Illegal VM state");
        }
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
    }
}
