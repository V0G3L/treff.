package org.pispeb.treff_client.view.home.groupList;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.databinding.GroupItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to hold {@link GroupListViewHolder}s and display them in a list
 */

public class GroupListAdapter extends Adapter<GroupListViewHolder>{

    private List<UserGroup> data;
    private GroupClickedListener groupClickedListener;

    public GroupListAdapter(GroupClickedListener groupClickedListener) {
        this.data = new ArrayList<>();
        this.groupClickedListener = groupClickedListener;
    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroupItemBinding binding = GroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupListViewHolder(binding, groupClickedListener);
    }

    @Override
    public void onBindViewHolder(GroupListViewHolder holder, int position) {
        holder.binding.setGroup(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<UserGroup> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public interface GroupClickedListener {
        void onItemClicked(int position, UserGroup group);
    }

}


/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link GroupListFragment}
 */

class GroupListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    final GroupItemBinding binding;
    private final GroupListAdapter.GroupClickedListener listener;
    public GroupListViewHolder(GroupItemBinding binding, GroupListAdapter.GroupClickedListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        binding.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition(), binding.getGroup());
    }
}
