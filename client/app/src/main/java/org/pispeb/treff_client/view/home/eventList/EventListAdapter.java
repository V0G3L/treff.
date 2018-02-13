package org.pispeb.treff_client.view.home.eventList;

import android.arch.paging.PagedListAdapter;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.OverlayItem;
import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.databinding.EventItemBinding;
import org.pispeb.treff_client.view.home.map.MapFragment;
import org.pispeb.treff_client.view.home.map.markers.EventMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to hold {@link EventListViewHolder}s and display them in a list
 */

public class EventListAdapter
        extends PagedListAdapter<Event, EventListViewHolder> {

    private EventClickedListener eventClickedListener;

    public EventListAdapter(EventClickedListener eventClickedListener) {
        super(diffCallback);
        this.eventClickedListener = eventClickedListener;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        EventItemBinding binding = EventItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new EventListViewHolder(binding, eventClickedListener);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        Event event = getItem(position);
        if (event != null) {
            holder.bindTo(event);
        }
    }

    public interface EventClickedListener {
        void onItemClicked(int position, Event event);
    }

    public static final DiffCallback<Event> diffCallback = new
            DiffCallback<Event>() {

        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem,
                                       @NonNull Event newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem,
                                          @NonNull Event newItem) {
            return oldItem.equals(newItem);
        }
    };
}

/**
 * Holding Layout for single item of the FriendList.
 * Passing on onCLick-events on that item to {@link EventListViewModel}
 */
class EventListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final EventItemBinding binding;
    private final EventListAdapter.EventClickedListener listener;
    private final IMapController controller;

    public EventListViewHolder(EventItemBinding binding,
                               EventListAdapter.EventClickedListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
        binding.getRoot().setOnClickListener(this);

        //binding.map.setEnabled(false);
        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        controller = binding.map.getController();
        controller.setZoom(MapFragment.STANDARD_ZOOM_LEVEL);
    }

    public void bindTo(Event event) {
        binding.setEvent(event);
        EventMarker marker = new EventMarker(binding.map, event);
        //marker.setIcon(Resources.getSystem().getDrawable(R.drawable
        //        .ic_marker_event));
        binding.map.getOverlays().add(marker);
        controller.setCenter(new GeoPoint(event.getLocation()));
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(getAdapterPosition(), binding.getEvent());
    }
}
