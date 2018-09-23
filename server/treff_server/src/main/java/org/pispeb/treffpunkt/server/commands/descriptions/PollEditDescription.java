package org.pispeb.treffpunkt.server.commands.descriptions;

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

    public PollEditDescription(String question,
                                         boolean isMultiChoice, Date timeVoteClose, int id) {
        super(question, isMultiChoice, timeVoteClose);
        this.id = id;
    }
}
