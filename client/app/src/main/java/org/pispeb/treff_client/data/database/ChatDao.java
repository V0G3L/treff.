package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.ChatMessage;

import java.util.List;

/**
 * {@link Dao} which provides access to {@link org.pispeb.treff_client.data.entities.Chat}s and {@link org.pispeb.treff_client.data.entities.ChatMessage}s
 */
@Dao
public interface ChatDao {
    //TODO chat and chatmessage entities (replace mockups)

    @Insert
    void save(ChatMessage message);

    @Query("SELECT * FROM message")
    LiveData<List<ChatMessage>> getAllMessages();
}
