package org.pispeb.treff_client.view.home.eventList;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.databinding.EventItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to hold {@link EventListViewHolder}s and display them in a list
 */

public class EventListAdapter extends Adapter<EventListViewHolder> {

    private List<Event> data;
    private EventClickedListener eventClickedListener;

    public EventListAdapter(EventClickedListener eventClickedListener) {
        this.data = new ArrayList<>();
        this.eventClickedListener = eventClickedListener;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventItemBinding binding = EventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventListViewHolder(binding, eventClickedListener);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        holder.binding.setEvent(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Event> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public interface EventClickedListener {
        void onItemClicked(int position, Event event);
    }
}

/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link EventListViewModel}
 */
class EventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final EventItemBinding binding;
    private final EventListAdapter.EventClickedListener listener;

    public EventListViewHolder(EventItemBinding binding, EventListAdapter.EventClickedListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        binding.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition(), binding.getEvent());
    }
}
