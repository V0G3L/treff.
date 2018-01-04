package org.pispeb.treff_client.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.entities.Event;


@Dao
public interface EventDao {
    @Insert
    void save(Event event);

    @Query("SELECT * FROM event WHERE id = :eventId")
    LiveData<Event> load(int eventId);
}
