package org.pispeb.treff_client.view.home.groupList;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.databinding.FragmentGroupListBinding;
import org.pispeb.treff_client.view.util.ViewModelFactory;

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

        binding.setVm(vm);

        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setHasFixedSize(true);

        return binding.getRoot();
    }
}
