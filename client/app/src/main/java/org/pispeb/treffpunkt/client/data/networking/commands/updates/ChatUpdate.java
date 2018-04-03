package org.pispeb.treffpunkt.client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.entities.ChatMessage;
import org.pispeb.treffpunkt.client.data.entities.User;
import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;

import java.util.Date;

public class ChatUpdate extends Update {

    private final int groupId;
    private final String message;

    public ChatUpdate(@JsonProperty("time-created") Date timeCreated,
                      @JsonProperty("creator") int creator,
                      @JsonProperty("group-id") int groupId,
                      @JsonProperty("message") String message) {
        super(timeCreated, creator);
        this.groupId = groupId;
        this.message = message;
    }

    @Override
    public void applyUpdate(RepositorySet repositorySet) {
        // creator == sender of chat message
        User sender = repositorySet.userRepository.getUser(creator);
        if (sender == null)
            sender = User.getPlaceholderAndScheduleQuery(creator, repositorySet.userRepository);

        ChatMessage chatMessage = new ChatMessage(groupId, message,
                sender.getUserId(), sender.getUsername(), timeCreated);

        repositorySet.chatRepository.addMessage(chatMessage);
    }
}
