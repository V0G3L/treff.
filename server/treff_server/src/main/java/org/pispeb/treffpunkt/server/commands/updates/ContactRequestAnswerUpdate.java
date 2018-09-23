package org.pispeb.treffpunkt.server.commands.updates;

import java.util.Date;

public class ContactRequestAnswerUpdate extends UpdateToSerialize{
    public final Boolean answer;

    public ContactRequestAnswerUpdate(Date date, int creator, Boolean answer) {
        super(UpdateType.CONTACT_REQUEST_ANSWER.toString(),
                date, creator);
        this.answer = answer;
    }
}
