package org.pispeb.treff_client.view.group.eventList;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.Occasion;
import org.pispeb.treff_client.data.entities.Poll;
import org.pispeb.treff_client.databinding.EventItemBinding;
import org.pispeb.treff_client.databinding.PollItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 1/7/2018.
 */

public class GroupEventListAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<Occasion> data;

    private final int IS_EVENT = 0;
    private final int IS_POLL = 1;

    public GroupEventListAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        Occasion o = data.get(position);
        if (o.getClass() == Event.class) {
            return IS_EVENT;
        } else if (o.getClass() == Poll.class) {
            return IS_POLL;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == IS_EVENT) {
            EventItemBinding binding = EventItemBinding.inflate
                    (LayoutInflater.from(parent.getContext()), parent, false);
            return new GroupEventListViewHolder(binding);
        } else {
            PollItemBinding binding = PollItemBinding.inflate(LayoutInflater
                    .from(parent.getContext()), parent, false);
            return new GroupPollListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == IS_EVENT) {
            ((GroupEventListViewHolder)holder).binding.setEvent((Event)data
                    .get(position));
        } else if (holder.getItemViewType() == IS_POLL) {
            ((GroupPollListViewHolder)holder).binding.setPoll((Poll)data
                    .get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Occasion> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}


class GroupEventListViewHolder extends RecyclerView.ViewHolder {

    final EventItemBinding binding;

    public GroupEventListViewHolder(EventItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

class GroupPollListViewHolder extends RecyclerView.ViewHolder {

    final PollItemBinding binding;

    public GroupPollListViewHolder(PollItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
