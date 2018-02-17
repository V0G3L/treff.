package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class CompletePoll {

    public final int id;
    public final String question;
    public final int creator;
    @JsonProperty("time-close")
    public final Date timeClose;
    @JsonProperty("multi-choice")
    public final boolean multiChoice;
    public final Object[] options;

    public CompletePoll(int id, String question, int creator, Date timeClose,
                        boolean multiChoice, Object[] options) {
        this.id = id;
        this.question = question;
        this.creator = creator;
        this.timeClose = timeClose;
        this.multiChoice = multiChoice;
        this.options = options;
    }
}
