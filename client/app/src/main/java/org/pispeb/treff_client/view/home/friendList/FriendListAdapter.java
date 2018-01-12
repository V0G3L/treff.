package org.pispeb.treff_client.view.home.friendList;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.databinding.FriendItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to hold {@link FriendListViewHolder}s and display them in a list
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


/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link FriendListFragment}
 */

class FriendListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final FriendItemBinding binding;
    private final FriendListAdapter.FriendClickedListener listener;

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
