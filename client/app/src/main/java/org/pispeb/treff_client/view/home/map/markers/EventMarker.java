package org.pispeb.treff_client.view.home.map.markers;

import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.pispeb.treff_client.data.entities.Event;

/**
 * Display events on the map
 */

public class EventMarker extends Marker {
    private Event event;

    public EventMarker(MapView mapView, Event event) {
        super(mapView);
        this.event = event;
        init();
    }

    public EventMarker(MapView mapView,
                       Context resourceProxy,
                       Event event) {
        super(mapView, resourceProxy);
        this.event = event;
        init();
    }

    private void init() {
        setPosition(new GeoPoint(event.getLocation()));
        setTitle(event.getName());
        setSnippet(event.getStart().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventMarker that = (EventMarker) o;

        return event.getId() == that.event.getId();
    }
}
