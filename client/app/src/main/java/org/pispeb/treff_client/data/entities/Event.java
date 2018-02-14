package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.location.LocationManager;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Room {@link Entity} that represents an event
 */

@Entity(tableName = "event")
public class Event extends Occasion{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date created;
    private Date start;
    private Location location;
    private int creator;

    public Event(String name, Date created, Date start, Location location, int creator) {
        this.name = name;
        this.created = created;
        this.start = start;
        this.location = location;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getStartString() {
        return start.toString().substring(0, 16);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (creator != event.creator) return false;
        if (created != null ? !created.equals(event.created) : event.created != null) return false;
        if (start != null ? !start.equals(event.start) : event.start != null) return false;
        return location != null ? location.equals(event.location) : event.location == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + creator;
        return result;
    }
}
