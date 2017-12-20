package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.components.Event;

/**
 * Created by Lukas on 12/20/2017.
 */

@Dao
public interface EventDao {
    @Insert
    void save(Event event);

    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<Event> load(int eventId);
}
