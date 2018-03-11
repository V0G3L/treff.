package org.pispeb.treffpunkt.client.view.home.eventList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.config.Configuration;
import org.pispeb.treffpunkt.client.databinding.FragmentEventListBinding;
import org.pispeb.treffpunkt.client.view.event.EventActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Fragment hosting RecyclerView of EventList and handling onClick-events
 * from items in the List
 */

public class EventListFragment extends Fragment {

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentEventListBinding binding = FragmentEventListBinding.inflate(inflater, container, false);
        EventListViewModel vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(getContext())).get(EventListViewModel.class);
        final EventListAdapter adapter = new EventListAdapter(vm);

        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));

        vm.getEvents().observe(this, adapter::setList);

        vm.getState().observe(this, this::callback);


        return binding.getRoot();
    }

    private void callback(State state) {
        switch (state.call) {
            case DISPLAY_EVENT_DETAILS:
                Intent eventIntent = new Intent(getContext(), EventActivity
                        .class);
                eventIntent.putExtra(EventActivity.EVENT_INTENT, state.value);
                startActivity(eventIntent);
                break;
        }
    }
}
