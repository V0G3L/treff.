package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Room {@link Entity} that represents an event
 */

@Entity(tableName = "event")
public class Event extends Occasion{
    @PrimaryKey
    private int id;
    private Date created;
    private Date start;
    private Position position;
    private int creator;

    public Event(int id, String name, int creator) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.start = start;
        this.position = position;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (creator != event.creator) return false;
        if (!created.equals(event.created)) return false;
        if (!start.equals(event.start)) return false;
        return position.equals(event.position);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + created.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + creator;
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public Date getStart() {
        return start;
    }

    public Position getPosition(){
        return position;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
