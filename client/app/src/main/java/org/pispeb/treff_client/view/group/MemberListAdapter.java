package org.pispeb.treff_client.view.group;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.User;
import org.pispeb.treff_client.databinding.MemberItemBinding;

/**
 * Adapter to hold {@link MemberListViewHolder}s and display them in a list
 */

public class MemberListAdapter
        extends PagedListAdapter<User, MemberListViewHolder> {

    private MemberClickedListener memberClickedListener;

    public MemberListAdapter(MemberClickedListener memberClickedListener) {
        super(diffCallback);
        this.memberClickedListener = memberClickedListener;
    }

    @Override
    public MemberListViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        MemberItemBinding binding = MemberItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new MemberListViewHolder(binding, memberClickedListener);
    }

    @Override
    public void onBindViewHolder(MemberListViewHolder holder, int position) {
        User user = getItem(position);
        if (user != null) {
            holder.bindTo(user);
        }
    }

    public interface MemberClickedListener {
        void onItemClicked(int position, User user);
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
 * Holding Layout for single item of the MemberList
 */

class MemberListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final MemberItemBinding binding;
    private final MemberListAdapter.MemberClickedListener listener;

    public MemberListViewHolder(MemberItemBinding binding,
                                MemberListAdapter.MemberClickedListener
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
