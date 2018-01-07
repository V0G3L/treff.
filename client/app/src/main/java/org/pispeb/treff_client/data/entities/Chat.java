package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Room {@link Entity} that represents the chat history of a {@link UserGroup}
 */

@Entity(tableName = "chat")
public class Chat {
    @PrimaryKey
    private int chatID;
    @Ignore //TODO messages
    private List<ChatMessage> messages;

    public Chat(int chatID) {
        this.chatID = chatID;
        //this.messages = messages;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
