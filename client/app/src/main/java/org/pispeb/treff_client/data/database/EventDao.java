package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.Event;

/**
 * Dao for events with methods to save events to the database or
 * load them all/based on group/based on id
 */

@Dao
public interface EventDao {
    @Insert
    void save(Event event);

    @Query("SELECT * FROM event WHERE id = :eventId")
    LiveData<Event> getEventByID(int eventId);
}
