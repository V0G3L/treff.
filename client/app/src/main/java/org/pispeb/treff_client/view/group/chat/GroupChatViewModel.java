package org.pispeb.treff_client.view.group.chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.data.repositories.ChatRepository;

import java.util.List;

public class GroupChatViewModel extends ViewModel {

    private int groupId;
    private LiveData<PagedList<ChatMessage>> messages;
    private final ChatRepository chatRepository;
    private String currentMessage;

    public GroupChatViewModel(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        currentMessage = "";
    }

    public LiveData<PagedList<ChatMessage>> getMessages() {
        return messages;
    }

    public void onSendClick() {
        if (!currentMessage.equals("")) {
            chatRepository.add(new ChatMessage(currentMessage, groupId));
            currentMessage = "";
        }
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }

    /**
     * Sets the group ID and updates the messages
     * @param groupId
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
        messages = chatRepository.getMessagesByGroupId(groupId);
    }
}
