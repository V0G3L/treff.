package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FragmentGroupListBinding;
import org.pispeb.treff_client.view.group.GroupActivity;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;
import org.pispeb.treff_client.view.util.ViewModelFactory;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Fragment hosting RecyclerView of GroupList and handling onClick-events
 * from items in the List.
 */

public class GroupListFragment extends Fragment {

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentGroupListBinding binding = FragmentGroupListBinding.inflate(inflater, container, false);

        GroupListViewModel vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(getContext())).get(GroupListViewModel.class);

        final GroupListAdapter adapter = new GroupListAdapter(vm);

        vm.getGroups().observe(this, groups -> {
            adapter.setData(groups);
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
            case IDLE: break;
            case DISPLAY_GROUP_DETAILS:
                GroupActivity.start(this, state.value);
                break;
            case ADD_GROUP:
                AddGroupActivity.start(this);
                break;
            default:
                Log.i("GroupList", "Illegal VM State");
        }
    }
}
