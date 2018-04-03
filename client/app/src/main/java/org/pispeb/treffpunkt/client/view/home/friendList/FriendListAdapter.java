package org.pispeb.treffpunkt.client.view.home.friendList;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.databinding.FriendItemBinding;

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
        void onAcceptClicked(User user);
        void onDeclineClicked(User user);
        void onCancelClicked(User user);
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

        binding.acceptButton.setOnClickListener(view
                -> listener.onAcceptClicked(binding.getUser()));
        binding.declineButton.setOnClickListener(view
                -> listener.onDeclineClicked(binding.getUser()));
        binding.cancelRequestButton.setOnClickListener(view
                -> listener.onCancelClicked(binding.getUser()));
    }

    public void bindTo(User user) {
        // databinding
        binding.setUser(user);
        binding.info.setVisibility(View.GONE);
        binding.acceptButton.setVisibility(View.GONE);
        binding.declineButton.setVisibility(View.GONE);
        binding.cancelRequestButton.setVisibility(View.GONE);
        if (user.isRequesting()) {
//            binding.info.setText(R.string.user_is_requesting);
            binding.acceptButton.setVisibility(View.VISIBLE);
            binding.declineButton.setVisibility(View.VISIBLE);
        } else if (user.isRequestPending()) {
            binding.info.setVisibility(View.VISIBLE);
            binding.info.setText(R.string.request_pending);
            binding.cancelRequestButton.setVisibility(View.VISIBLE);
        } else if (user.isBlocked()) {
            binding.info.setVisibility(View.VISIBLE);
            binding.info.setText(R.string.blocked);
        } // no info-text or buttons for regular friends
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition(), binding.getUser());
    }
}
