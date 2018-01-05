package org.pispeb.treff_client.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Pojo containing messages from users of a group
 */

@Entity(tableName = "chat")
public class Chat {
    @PrimaryKey
    private int chatID;
    private List<ChatMessage> messages;

    public Chat(int chatID, List<ChatMessage> messages) {
        this.chatID = chatID;
        this.messages = messages;
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
