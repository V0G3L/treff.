package org.pispeb.treff_client.view.group.chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.data.repositories.ChatRepository;

import java.util.List;

/**
 * Created by Lukas on 1/6/2018.
 */

public class GroupChatViewModel extends ViewModel {

    private LiveData<List<ChatMessage>> messages;
    private final ChatRepository chatRepository;
    private String currentMessage;

    public GroupChatViewModel(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.messages = chatRepository.getAllMessages();
        currentMessage = "";
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    public void onSendClick() {
        chatRepository.add(new ChatMessage((int)System.currentTimeMillis(), currentMessage));
        currentMessage = "";
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }
}
