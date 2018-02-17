package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class ChatUpdate extends UpdateToSerialize {

    @JsonProperty("group-id")
    int groupId;
    @JsonProperty("message")
    String message;

    public ChatUpdate(Date date, int creator,
                             int groupId, String message) {
        super(Update.UpdateType.CHAT.toString(), date, creator);
        this.groupId = groupId;
        this.message = message;
    }
}
