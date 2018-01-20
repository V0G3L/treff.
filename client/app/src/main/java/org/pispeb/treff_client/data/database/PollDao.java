package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.Poll;

import java.util.List;

/**
 * {@link Dao} which provides access to {@link org.pispeb.treff_client.data.entities.Poll}s and {@link org.pispeb.treff_client.data.entities.PollOption}s
 */
@Dao
public interface PollDao {
    @Insert
    void save(Poll poll);

    @Query("SELECT * FROM poll WHERE pollID = :pollID")
    LiveData<Poll> getPollByID(int pollID);

    @Query("SELECT * FROM poll")
    DataSource.Factory<Integer, Poll> getAllPolls();

}
