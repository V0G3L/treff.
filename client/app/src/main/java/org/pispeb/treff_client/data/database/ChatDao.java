package org.pispeb.treff_client.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.pispeb.treff_client.data.entities.ChatMessage;

import java.util.List;
import java.util.Set;

/**
 * {@link Dao} which provides access to {@link org.pispeb.treff_client.data.entities.ChatMessage}s
 */
@Dao
public interface ChatDao {

    @Insert
    void save(ChatMessage message);

    @Query("SELECT * FROM message WHERE groupID = :groupId ORDER BY messageId" +
            " DESC")
    DataSource.Factory<Integer, ChatMessage> getMessagesByGroupId(int groupId);

    @Query("SELECT * FROM message WHERE groupID = :groupId ORDER BY messageId" +
            " DESC")
    List<ChatMessage> getMessageListByGroupId(int groupId);

    @Delete
    void deleteMessages(List<ChatMessage> messages);

    @Query("DELETE FROM message")
    void deleteAllMessages();
}
