package org.pispeb.treff_client.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by Lukas on 12/19/2017.
 */

@Entity(tableName = "usergroup")
public class UserGroup {
    @PrimaryKey(autoGenerate = true)
    private List<User> users;
    @ColumnInfo
    private List<Event> events;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private int id;
}
