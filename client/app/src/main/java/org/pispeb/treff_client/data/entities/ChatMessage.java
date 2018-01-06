package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * Created by Lukas on 1/5/2018.
 */

@Entity(tableName = "message")
public class ChatMessage {
    @PrimaryKey
    private int messageID;
    private String content;
    private User sender;
    private LocalDateTime timeSent;

    public ChatMessage(int messageID, String content, User sender, LocalDateTime timeSent) {
        this.messageID = messageID;
        this.content = content;
        this.sender = sender;
        this.timeSent = timeSent;
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
