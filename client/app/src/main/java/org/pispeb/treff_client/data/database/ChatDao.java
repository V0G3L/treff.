package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.ChatMessage;

import java.util.List;

/**
 * {@link Dao} which provides access to {@link org.pispeb.treff_client.data.entities.ChatMessage}s
 */
@Dao
public interface ChatDao {
    //TODO chat and chatmessage entities (replace mockups)

    @Insert
    void save(ChatMessage message);

    @Query("SELECT * FROM message WHERE groupID = :groupId")
    DataSource.Factory<Integer, ChatMessage> getMessagesByGroupId(int groupId);
}
