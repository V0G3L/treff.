package org.pispeb.treffpunkt.client.view.home.map.markers;

import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.pispeb.treffpunkt.client.data.entities.User;

import java.util.Date;

/**
 * Marker than can be displayed on OSM containing info about the user the
 * marker points to
 */

public class UserMarker extends Marker {

    private User user;

    public UserMarker(MapView mapView, User user) {
        super(mapView);
        this.user = user;
        init();
    }

    public UserMarker(MapView mapView,
                      Context resourceProxy,
                      User user) {
        super(mapView, resourceProxy);
        this.user = user;
        init();
    }

    private void init() {
        setPosition(new GeoPoint(user.getLocation()));
        setTitle(user.getUsername());
        Date d = new Date(user.getLocation().getTime());
        setSnippet(d.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMarker that = (UserMarker) o;

        return user.getUserId() == that.user.getUserId();
    }
}
