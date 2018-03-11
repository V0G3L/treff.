package org.pispeb.treff_client.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Room {@link Entity} that represents a single chat message
 */
@Entity(tableName = "message")
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private int messageId;
    private int groupId;
    private String content;
    private int userId;
    private String username;
    private Date timeSent;

    public ChatMessage(int groupId,
                       String content,
                       int userId,
                       String username,
                       Date timeSent) {
        this.groupId = groupId;
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.timeSent = timeSent;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public boolean equalContent(ChatMessage o) {
        if (groupId != o.groupId) return false;
        if (userId != o.userId) return false;
        if (content != null ? !content.equals(o.content) : o.content != null) {
            return false;
        }
        return timeSent != null ? timeSent.equals(o.timeSent) : o.timeSent ==
                null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (messageId != that.messageId) return false;
        if (groupId != that.groupId) return false;
        if (userId != that.userId) return false;
        if (content != null ? !content
                .equals(that.content) : that.content != null) {
            return false;
        }
        return timeSent != null ? timeSent
                .equals(that.timeSent) : that.timeSent == null;
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + groupId;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + (timeSent != null ? timeSent.hashCode() : 0);
        return result;
    }
}
