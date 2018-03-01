package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Immutable complete description of a poll as specified in the
 * treffpunkt protocol document, lacking the id and creator property and the
 * options array.
 * <p>
 * Poll create commands should require an instance of this class as input.
 */
public class PollCreateDescription {

    public final String question;
    public final boolean isMultiChoice;
    public final Date timeVoteClose;

    public PollCreateDescription(@JsonProperty("question") String question,
                                 @JsonProperty("multi-choice")
                                         boolean isMultiChoice,
                                 @JsonProperty("time-close") Date timeVoteClose) {
        this.question = question;
        this.isMultiChoice = isMultiChoice;
        this.timeVoteClose = timeVoteClose;
    }

    public boolean equals(PollCreateDescription pollCreateDescription) {
        return (this.question.equals(pollCreateDescription.question)
                && this.isMultiChoice == pollCreateDescription.isMultiChoice
                && this.timeVoteClose.equals(pollCreateDescription.timeVoteClose));
    }
}
