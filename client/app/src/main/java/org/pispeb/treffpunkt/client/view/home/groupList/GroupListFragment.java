package org.pispeb.treffpunkt.client.view.home.groupList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.databinding.FragmentGroupListBinding;
import org.pispeb.treffpunkt.client.view.group.GroupActivity;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Fragment hosting RecyclerView of GroupList and handling onClick-events
 * from items in the List.
 */

public class GroupListFragment extends Fragment {

    private GroupListViewModel vm;
    private GroupListAdapter adapter;

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create ViewModel
        vm = ViewModelProviders.of(this, ViewModelFactory.getInstance(getContext())).get(GroupListViewModel.class);

        // create adapter
        adapter = new GroupListAdapter(vm);

        // update adapter when groups change
        vm.getGroups().observe(this, groups -> {
            adapter.setList(groups);
        });

        // react to vm callbacks
        vm.getState().observe(this, state -> callback(state));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final FragmentGroupListBinding binding = FragmentGroupListBinding.inflate(inflater, container, false);

        // set up databinding
        binding.setVm(vm);

        // set up adapter in view
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        return binding.getRoot();
    }

    private void callback(State state) {
        switch (state.call) {
            case IDLE: break;
            case DISPLAY_GROUP_DETAILS:
                int groupID = state.value;
                Intent groupIntent = new Intent(getContext(), GroupActivity
                        .class);
                groupIntent.putExtra(GroupActivity.GRP_INTENT, groupID);
                startActivity(groupIntent);
                break;
            case ADD_GROUP:
                Intent addGroupIntent = new Intent(getContext(),
                        AddGroupActivity.class);
                startActivity(addGroupIntent);
                break;
            default:
                Log.i("GroupList", "Illegal VM State");
        }
    }
}
