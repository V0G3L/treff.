package org.pispeb.treff_client.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * Pojo defining an event in a group
 */

@Entity(tableName = "event")
public class Event {
    @PrimaryKey
    private int id;
    private String name;
    //TODO replace
    //private LocalDateTime created;
    //private LocalDateTime start;
    //private Position position;
    private int creator;

    public Event(int id, String name, int creator) {
        this.id = id;
        this.name = name;
        //TODO fix
        //this.created = created;
        //this.start = start;
        //this.position = position;
        this.creator = creator;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    /* TODO fix
    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Position getPosition(){
        return position;
    }


    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    */
}
