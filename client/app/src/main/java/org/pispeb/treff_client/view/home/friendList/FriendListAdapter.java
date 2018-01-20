package org.pispeb.treff_client.view.home.friendList;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.databinding.FriendItemBinding;

/**
 * Adapter to hold {@link FriendListViewHolder}s and display them in a list
 */

public class FriendListAdapter
        extends PagedListAdapter<User, FriendListViewHolder> {

    private FriendClickedListener friendClickedListener;

    public FriendListAdapter(FriendClickedListener friendClickedListener) {
        super(diffCallback);
        this.friendClickedListener = friendClickedListener;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        FriendItemBinding binding = FriendItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new FriendListViewHolder(binding, friendClickedListener);
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        User user = getItem(position);
        if (user != null) {
            holder.bindTo(user);
        }
    }

    public interface FriendClickedListener {
        void onItemClicked(int position, User user);
    }

    public static final DiffCallback<User> diffCallback = new
            DiffCallback<User>() {

                @Override
                public boolean areItemsTheSame(@NonNull User oldItem,
                                               @NonNull User newItem) {
                    return oldItem.getUserID() == newItem.getUserID();
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

class FriendListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final FriendItemBinding binding;
    private final FriendListAdapter.FriendClickedListener listener;

    public FriendListViewHolder(FriendItemBinding binding,
                                FriendListAdapter.FriendClickedListener
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
        listener.onItemClicked(getAdapterPosition(), binding.getUser());
    }
}
