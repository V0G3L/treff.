package org.pispeb.treff_client.lists;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import org.pispeb.treff_client.databinding.FriendItemBinding;


public class FriendListViewHolder extends RecyclerView.ViewHolder {

    final FriendItemBinding binding;

    public FriendListViewHolder(View itemView, FriendItemBinding binding) {
        super(itemView);
        this.binding = binding;
    }

}
