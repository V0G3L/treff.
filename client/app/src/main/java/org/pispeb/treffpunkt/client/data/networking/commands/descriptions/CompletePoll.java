package org.pispeb.treffpunkt.client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by matth on 17.02.2018.
 */

public class CompletePoll {

    public final String type;
    public final int id;
    public final String question;
    public final int creator;
    @JsonProperty("time-close")
    public final Date timeClose;
    @JsonProperty("multi-choice")
    public final boolean multiChoice;
    public final Object[] options;

    public CompletePoll(@JsonProperty("poll") String type,
                        @JsonProperty("id") int id,
                        @JsonProperty("question") String question,
                        @JsonProperty("creator") int creator,
                        @JsonProperty("time-close") Date timeClose,
                        @JsonProperty("multi-choice") boolean multiChoice,
                        @JsonProperty("options") Object[] options) {

        this.type = "poll";
        this.id = id;
        this.question = question;
        this.creator = creator;
        this.timeClose = timeClose;
        this.multiChoice = multiChoice;
        this.options = options;
    }
}
