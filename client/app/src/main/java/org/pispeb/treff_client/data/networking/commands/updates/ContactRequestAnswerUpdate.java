package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ContactRequestAnswerUpdate extends UpdateToSerialize{
    public final Boolean answer;

    public ContactRequestAnswerUpdate(@JsonProperty("type") String type,
                                      @JsonProperty("time-created") Date date,
                                      @JsonProperty("creator") int creator,
                                      @JsonProperty("answer") Boolean answer) {
        super(UpdateType.CONTACT_REQUEST_ANSWER.toString(),
                date, creator);
        this.answer = answer;
    }
}
