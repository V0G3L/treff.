package org.pispeb.treff_client.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * Created by Lukas on 12/19/2017.
 */
@Entity(tableName = "event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private LocalDateTime created;
    @ColumnInfo
    private LocalDateTime start;
    @ColumnInfo
    private Position position;

    public Event(int id, String name, LocalDateTime created, LocalDateTime start) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.start = start;
    }
}
