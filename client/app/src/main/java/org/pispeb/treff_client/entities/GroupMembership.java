package org.pispeb.treff_client.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class GroupMembership {
    @PrimaryKey(autoGenerate = true)
    int id;
    int userID;
    int groupID;
}
