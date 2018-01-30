package org.pispeb.treff_client.view.group.chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import org.pispeb.treff_client.data.entities.ChatMessage;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.view.util.SingleLiveEvent;
import org.pispeb.treff_client.view.util.State;
import org.pispeb.treff_client.view.util.ViewCall;

import java.util.Date;
import java.util.List;

public class GroupChatViewModel extends ViewModel {

    private int groupId;
    private LiveData<PagedList<ChatMessage>> messages;
    private final ChatRepository chatRepository;
    private String currentMessage;

    private SingleLiveEvent<State> state;

    public GroupChatViewModel(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.state = new SingleLiveEvent<>();
        currentMessage = "";
    }

    public LiveData<PagedList<ChatMessage>> getMessages() {
        return messages;
    }

    public void onSendClick() {
        if (!currentMessage.equals("")) {
            //TODO set the proper user ID e.g. by getting it from SharedPreferences
            chatRepository.add(new ChatMessage(groupId, currentMessage, 1, new Date()));
            currentMessage = "";
            state.postValue(new State(ViewCall.UPDATE_VIEW, 0));
        }
    }

    public SingleLiveEvent<State> getState() {
        return state;
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
