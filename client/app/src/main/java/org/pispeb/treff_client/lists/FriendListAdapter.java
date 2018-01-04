package org.pispeb.treff_client.lists;

import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FriendItemBinding;
import org.pispeb.treff_client.entities.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lukas on 12/19/2017.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListViewHolder> {

    List<User> data;

    public FriendListAdapter(List<User> data) {
        this.data = data;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendItemBinding binding = FriendItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FriendListViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        holder.binding.setName(data.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
