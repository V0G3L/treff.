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
    private int groupID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (messageID != that.messageID) return false;
        if (!content.equals(that.content)) return false;
        if (!sender.equals(that.sender)) return false;
        return timeSent.equals(that.timeSent);
    }

    @Override
    public int hashCode() {
        int result = messageID;
        result = 31 * result + content.hashCode();
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (timeSent != null ? timeSent.hashCode() : 0);
        return result;
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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
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
