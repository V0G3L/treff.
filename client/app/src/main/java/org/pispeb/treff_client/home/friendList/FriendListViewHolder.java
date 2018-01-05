package org.pispeb.treff_client.home.friendList;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;

import org.pispeb.treff_client.databinding.FriendItemBinding;

/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link FriendListFragment}
 */

public class FriendListViewHolder extends ViewHolder implements OnClickListener {

    final FriendItemBinding binding;
    private FriendListAdapter.FriendClickedListener listener;

    public FriendListViewHolder(FriendItemBinding binding, FriendListAdapter.FriendClickedListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        binding.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition(), binding.getUser());
    }
}
