package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Handler;

import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;

/**
 * {@link ChatMessage} repository serving as a bridge between ViewModels and
 * local data/network requests
 */
public class ChatRepository {
    private ChatDao chatDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    /**
     * Creates a new {@link ChatRepository}
     *
     * @param chatDao           chatDao
     * @param encoder           requestEncoder
     * @param backgroundHandler backgroundHandler
     */
    public ChatRepository(ChatDao chatDao, RequestEncoder encoder,
                          Handler backgroundHandler) {
        this.chatDao = chatDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    /**
     * Adds a new {@link ChatMessage}
     *
     * @param message the message
     */
    public void addMessage(ChatMessage message) {
        backgroundHandler.post(() -> {
            chatDao.save(message);
        });
    }

    /**
     * Returns a {@link PagedList} of {@link ChatMessage}s of a
     * {@link org.pispeb.treff_client.data.entities.UserGroup}
     *
     * @param groupId the group's unique identifier
     * @return {@link PagedList} of {@link ChatMessage}s
     */
    public LiveData<PagedList<ChatMessage>> getMessagesByGroupId(int groupId) {
        return new LivePagedListBuilder<>(chatDao.getMessagesByGroupId(groupId),
                30).build();
    }

    /**
     * Sends a request to the server in order to send a message
     * @param groupId group to which the message is sent
     * @param message content of the message
     */
    public void requestSendMessage(int groupId, String message) {
        encoder.sendChatMessage(groupId, message);
    }
}
