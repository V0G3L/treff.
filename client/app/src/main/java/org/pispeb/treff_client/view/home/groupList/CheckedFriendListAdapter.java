package org.pispeb.treff_client.view.home.groupList;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.databinding.CheckedFriendItemBinding;
import org.pispeb.treff_client.databinding.FriendItemBinding;
import org.pispeb.treff_client.view.home.friendList.FriendListFragment;


//TODO merge with regular FriendList

/**
 * Adapter to hold {@link CheckedFriendListViewHolder}s and display them in a list
 */

public class CheckedFriendListAdapter
        extends PagedListAdapter<User, CheckedFriendListViewHolder> {

    private FriendClickedListener friendClickedListener;

    public CheckedFriendListAdapter(FriendClickedListener friendClickedListener) {
        super(diffCallback);
        this.friendClickedListener = friendClickedListener;
    }

    @Override
    public CheckedFriendListViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        CheckedFriendItemBinding binding = CheckedFriendItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new CheckedFriendListViewHolder(binding, friendClickedListener);
    }

    @Override
    public void onBindViewHolder(
            CheckedFriendListViewHolder holder, int position) {
        User user = getItem(position);
        if (user != null) {
            holder.bindTo(user);
        }
    }

    public interface FriendClickedListener {
        void onItemClicked(int position, User user, boolean isChecked);
    }

    public static final DiffCallback<User> diffCallback = new
            DiffCallback<User>() {

                @Override
                public boolean areItemsTheSame(@NonNull User oldItem,
                                               @NonNull User newItem) {
                    return oldItem.getUserId() == newItem.getUserId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull User oldItem,
                                                  @NonNull User newItem) {
                    return oldItem.equals(newItem);
                }
            };
}


/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link FriendListFragment}
 */

class CheckedFriendListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final CheckedFriendItemBinding binding;
    private final CheckedFriendListAdapter.FriendClickedListener listener;

    public CheckedFriendListViewHolder(CheckedFriendItemBinding binding,
                                CheckedFriendListAdapter.FriendClickedListener
                                        listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        binding.getRoot().setOnClickListener(this);
    }

    public void bindTo(User user) {
        binding.setUser(user);
    }

    @Override
    public void onClick(View v) {
        binding.checkbox.toggle();
        boolean isChecked = binding.checkbox.isChecked();
        listener.onItemClicked(getAdapterPosition(), binding.getUser(), isChecked);
    }
}
