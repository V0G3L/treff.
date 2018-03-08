package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ChatUpdate extends UpdateToSerialize {

    public final int groupId;
    public final String message;

    public ChatUpdate(@JsonProperty("type") String type,
                      @JsonProperty("time-created") Date date,
                      @JsonProperty("creator") int creator,
                      @JsonProperty("group-id") int groupId,
                      @JsonProperty("message") String message) {
        super(UpdateType.CHAT.toString(), date, creator);
        this.groupId = groupId;
        this.message = message;
    }
}
