package org.pispeb.treffpunkt.client.view.group.chat;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.entities.ChatMessage;
import org.pispeb.treffpunkt.client.databinding.ChatItemBinding;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;

/**
 * Adapter to hold {@link GroupChatViewHolder]}s and display them in a list,
 * representing a chat history.
 */
public class GroupChatAdapter
        extends PagedListAdapter<ChatMessage, GroupChatViewHolder> {

    public GroupChatAdapter() {
        super(diffCallback);
    }

    @Override
    public GroupChatViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        ChatItemBinding binding = ChatItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new GroupChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GroupChatViewHolder holder, int position) {
        ChatMessage message = getItem(position);
        if (message != null) {
            holder.bindTo(message);
        }
    }

    public static final DiffCallback<ChatMessage> diffCallback = new DiffCallback<ChatMessage>() {


        @Override
        public boolean areItemsTheSame(@NonNull ChatMessage oldItem,
                                       @NonNull ChatMessage newItem) {
            return oldItem.getMessageId() == newItem.getMessageId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatMessage oldItem,
                                          @NonNull ChatMessage newItem) {
            return oldItem.equals(newItem);
        }
    };
}

class GroupChatViewHolder extends RecyclerView.ViewHolder {

    private final ChatItemBinding binding;

    public GroupChatViewHolder(ChatItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindTo(ChatMessage message) {
        binding.setMessage(message);
        Context appctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(appctx);
        int id = pref.getInt(appctx.getString(R.string.key_userId), -1);
        if (message.getUserId() == id) {
            //own
            binding.text.getBackground().setTint(appctx.getColor(R.color.colorPrimary));
        } else {
            //others
            binding.text.getBackground().setTint(appctx.getColor(R.color.lightGrey));
        }
    }
}

