package org.pispeb.treff_client.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "usergroup")
public class UserGroup {
    @PrimaryKey
    private int id;
    private String name;
    //TODO replace
    //private List<Integer> userIDs;
    //private List<Integer> eventIDs;

    public UserGroup(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
