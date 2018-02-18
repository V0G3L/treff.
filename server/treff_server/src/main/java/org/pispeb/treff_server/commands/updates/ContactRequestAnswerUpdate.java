package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class ContactRequestAnswerUpdate extends UpdateToSerialize{
    @JsonProperty("answer")
    public final Boolean answer;

    public ContactRequestAnswerUpdate(Date date, int creator, Boolean answer) {
        super(Update.UpdateType.CONTACT_REQUEST_ANSWER.toString(),
                date, creator);
        this.answer = answer;
    }
}
