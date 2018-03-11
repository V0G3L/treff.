package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Immutable complete description of a poll as specified in the
 * treffpunkt protocol document, lacking the creator property and the options
 * array.
 * <p>
 * Poll edit commands should require an instance of this class as input.
 */
public class PollEditDescription extends PollCreateDescription {

    public final int id;

    public PollEditDescription(@JsonProperty("type") String type,
                               @JsonProperty("question") String question,
                               @JsonProperty("multi-choice")
                                         boolean isMultiChoice,
                               @JsonProperty("time-close") Date timeVoteClose,
                               @JsonProperty("id") int id) {
        super(type, question, isMultiChoice, timeVoteClose);
        this.id = id;
    }

    public boolean equals(PollEditDescription pollEditDescription) {
        return (super.equals(pollEditDescription) && this.id == pollEditDescription.id);
    }
}
