package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * Room {@link Entity} that represents a single chat message in a {@link Chat}
 */
@Entity(tableName = "message")
public class ChatMessage {
    @PrimaryKey
    private int messageID;
    private String content;
    @Ignore //TODO user
    private User sender;
    @Ignore //TODO timestamp
    private LocalDateTime timeSent;

    public ChatMessage(int messageID, String content) {
        this.messageID = messageID;
        this.content = content;
        //this.sender = sender;
        //this.timeSent = timeSent;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}
