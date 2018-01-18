package org.pispeb.treff_client.data.repositories;

import android.arch.lifecycle.LiveData;
import android.os.Handler;

import org.pispeb.treff_client.data.database.ChatDao;
import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.data.networking.RequestEncoder;

import java.util.List;

public class ChatRepository {
    private ChatDao chatDao;
    private RequestEncoder encoder;
    private Handler backgroundHandler;

    public ChatRepository(ChatDao chatDao, RequestEncoder encoder, Handler backgroundHandler) {
        this.chatDao = chatDao;
        this.encoder = encoder;
        this.backgroundHandler = backgroundHandler;
    }

    public void add(ChatMessage message) {
        backgroundHandler.post(() -> {
            chatDao.save(message);
        });
    }

    public LiveData<List<ChatMessage>> getAllMessages() {
        return chatDao.getAllMessages();
    }

//    TODO implement
    public LiveData<List<ChatMessage>> getMessageByGroupId(int groupId) {
        return chatDao.getAllMessages();
    }
}
