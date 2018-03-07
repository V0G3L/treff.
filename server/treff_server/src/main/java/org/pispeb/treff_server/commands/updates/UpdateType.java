package org.pispeb.treff_server.commands.updates;

import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.interfaces.Usergroup;

/**
 * The type of an {@link Update}.
 */
public enum UpdateType {
    /**
     * The type of updates that represent changes to an account
     */
    ACCOUNT_CHANGE,
    /**
     * The type of updates that represent a deletion of an account
     */
    ACCOUNT_DELETION,
    /**
     * The type of updates that represent changes to an usergroup
     */
    USERGROUP_CHANGE,
    /**
     * The type of updates that represent changes to an event
     */
    EVENT_CHANGE,
    /**
     * The type of updates that represent deletion of an event
     */
    EVENT_DELETION,
    /**
     * The type of updates that represent changes to a poll
     */
    POLL_CHANGE,
    /**
     * The type of updates that represent changes to a poll-option
     */
    POLL_OPTION_CHANGE,
    /**
     * The type of updates that represent changes to an account
     */
    GROUP_MEMBERSHIP_CHANGE,

    /**
     * The type of updates that represent chat messages sent to
     * {@link Usergroup}s
     */
    CHAT,
    /**
     * The type of updates that represent a position request
     */
    POSITION_REQUEST,
    /**
     * The type of updates that represent a contact request
     */
    CONTACT_REQUEST,
    /**
     * The type of updates that represent a position change
     */
    POSITION,
    /**
     * The type of updates that represent an answer to a contact request
     */
    CONTACT_REQUEST_ANSWER,
    /**
     * The type of updates that represent someone canceling a contact
     * request
     */
    CANCEL_CONTACT_REQUEST,
    /**
     * The type of updates that represent someone removing a contact
     */
    REMOVE_CONTACT;

    @Override
    public String toString() {
        return this.name().toLowerCase().replace('_','-');
    }
}
