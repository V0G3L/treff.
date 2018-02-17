package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.UserGroup;

import java.util.List;
import java.util.Set;

/**
 * {@link Dao} which provides access to {@link Event}s
 */

@Dao
public interface EventDao {
    @Insert
    void save(Event event);

    @Query("SELECT * FROM event WHERE id = :eventId")
    LiveData<Event> getEventByID(int eventId);

    @Query("SELECT * FROM event")
    DataSource.Factory<Integer, Event> getAllEvents();

    @Query("SELECT * FROM event WHERE id IN(:eventIds)")
    List<Event> getEventsByIdSet(Set<Integer> eventIds);

    @Delete
    void deleteEvents(List<Event> events);

//    @Query("SELECT * FROM event INNER JOIN usergroup " +
//            "ON usergroup.event = usergroup.event " +
//            "WHERE usergroup.groupID = :g.groupID")
//    DataSource.Factory<Integer, Event> getEventsFromGroups(Set<UserGroup> g);

    @Query("SELECT * FROM event INNER JOIN usergroup " +
            "ON usergroup.event = usergroup.event " +
            "WHERE usergroup.groupID = :g.groupId")
    DataSource.Factory<Integer, Event> getEventsFromGroup(UserGroup g);


    @Update
    void update(Event event);

    @Delete
    void delete(Event event);
}
