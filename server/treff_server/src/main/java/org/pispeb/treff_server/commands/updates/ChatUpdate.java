package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ChatUpdate extends UpdateToSerialize {

    @JsonProperty("group-id")
    public final int groupId;
    @JsonProperty("message")
    public final String message;

    public ChatUpdate(Date date, int creator,
                             int groupId, String message) {
        super(UpdateType.CHAT.toString(), date, creator);
        this.groupId = groupId;
        this.message = message;
    }
}
