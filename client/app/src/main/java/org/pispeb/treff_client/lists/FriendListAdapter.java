package org.pispeb.treff_client.lists;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pispeb.treff_client.databinding.FriendItemBinding;
import org.pispeb.treff_client.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 12/19/2017.
 */

public class FriendListAdapter extends Adapter<FriendListViewHolder> {

    private List<User> data;
    private FriendClickedListener friendClickedListener;

    public FriendListAdapter(FriendClickedListener friendClickedListener) {
        this.data = new ArrayList<>();
        this.friendClickedListener = friendClickedListener;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendItemBinding binding = FriendItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FriendListViewHolder(binding, friendClickedListener);
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        holder.binding.setUser(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public interface FriendClickedListener {
        void onItemClicked(int position, User user);
    }
}
