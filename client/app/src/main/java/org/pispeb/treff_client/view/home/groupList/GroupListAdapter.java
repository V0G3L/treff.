package org.pispeb.treff_client.view.home.groupList;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.databinding.GroupItemBinding;

/**
 * Adapter to hold {@link GroupListViewHolder}s and display them in a list
 */

public class GroupListAdapter extends PagedListAdapter<UserGroup,
        GroupListViewHolder> {

    private GroupClickedListener groupClickedListener;

    public GroupListAdapter(GroupClickedListener groupClickedListener) {
        super(diffCallback);
        this.groupClickedListener = groupClickedListener;
    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        GroupItemBinding binding = GroupItemBinding
                .inflate(LayoutInflater
                        .from(parent.getContext()), parent, false);
        return new GroupListViewHolder(binding, groupClickedListener);
    }

    @Override
    public void onBindViewHolder(GroupListViewHolder holder, int position) {
        UserGroup group = getItem(position);
        if (group != null) {
            holder.bindTo(getItem(position));
        }
    }

    public static final DiffCallback<UserGroup> diffCallback = new
            DiffCallback<UserGroup>() {


                @Override
                public boolean areItemsTheSame(@NonNull UserGroup oldItem,
                                               @NonNull UserGroup newItem) {
                    return oldItem.getGroupId() == newItem.getGroupId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull UserGroup oldItem,
                                                  @NonNull UserGroup newItem) {
                    return false;
                }
            };

    public interface GroupClickedListener {
        void onItemClicked(int position, UserGroup group);
    }
}


/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link GroupListFragment}
 */

class GroupListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    final GroupItemBinding binding;
    private final GroupListAdapter.GroupClickedListener listener;

    public GroupListViewHolder(GroupItemBinding binding,
                               GroupListAdapter.GroupClickedListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        binding.getRoot().setOnClickListener(this);
    }

    public void bindTo(UserGroup group) {
        binding.setGroup(group);
        if (group.isSharingLocation()) {
            binding.sharing.setVisibility(View.VISIBLE);
        } else {
            binding.sharing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition(), binding.getGroup());
    }
}
