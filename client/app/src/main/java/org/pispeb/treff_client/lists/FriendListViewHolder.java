package org.pispeb.treff_client.lists;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;

import org.pispeb.treff_client.databinding.FriendItemBinding;


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
