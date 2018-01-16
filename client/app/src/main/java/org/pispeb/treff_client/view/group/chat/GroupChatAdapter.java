package org.pispeb.treff_client.view.group.chat;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.databinding.ChatItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 1/6/2018.
 */

public class GroupChatAdapter extends Adapter<GroupChatViewHolder> {

    private List<ChatMessage> data;

    public GroupChatAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public GroupChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatItemBinding binding = ChatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GroupChatViewHolder holder, int position) {
        holder.binding.setMessage(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ChatMessage> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

class GroupChatViewHolder extends RecyclerView.ViewHolder {

    final ChatItemBinding binding;

    public GroupChatViewHolder(ChatItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

