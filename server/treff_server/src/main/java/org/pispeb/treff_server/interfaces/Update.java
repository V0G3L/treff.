package org.pispeb.treff_server.interfaces;


import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Represents a change to the database caused by user interaction that
 * affects other {@link Account}s.
 * This change can be either an edit to an entity or a chat message sent to a
 * {@link Usergroup}.
 * <p>
 * <p>Once an {@link Update} is sent to a client device using an {@link Account}
 * that is affected by that UpdateToSerialize, the Account should be removed from the
 * UpdateToSerialize's set of affected Accounts to ensure that the same Account isn't
 * handed
 * the same UpdateToSerialize twice.
 * An UpdateToSerialize that has no more affected Accounts should be isDeleted and no longer
 * referenced.</p>
 * <p>
 * <p>Updates are naturally ordered from oldest to newest.</p>
 */
public interface Update extends Comparable<Update>, DataObject {

    /**
     * Returns the time the {@link Update} was created, used for ordering
     *
     * @return The time the UpdateToSerialize was created
     */
    Date getTime();

    /**
     * Returns the content of the UpdateToSerialize.
     *
     * @return A String that is a JSON-Object containing
     * all necessary IDs used for referencing in the
     * communication protocol, and either the names of the edited fields and
     * their
     * values or the chat message.
     * <p>The structure of the JsonObject depends on the type.
     * For an {@link UpdateType#EDIT} UpdateToSerialize, the format is a partial extensive
     * description omitting all non-id fields that didn't change.</p>
     * <p>For a {@link UpdateType#CHAT} UpdateToSerialize, the format is as follows:</p>
     * <code>{ "group-id": id-of-group, "message": the-message }</code>
     */
    String getUpdate();

    /**
     * Removes an {@link Account} from the set of affected Accounts.
     * Should the set be empty after the removal, this method will
     * return true.
     * In that case, the UpdateToSerialize should be considered as isDeleted.
     * It's methods should no longer be used and all references to it
     * should be removed.
     *
     * @param account The Account to be removed
     * @return <code>true</code> if the set of affected Accounts is
     * empty after removal, <code>false</code> otherwise.
     */
    boolean removeAffectedAccount(Account account);

    /**
     * Returns the set of affected {@link Account}.
     *
     * @return The set of affected Accounts
     */
    Map<Integer, Account> getAffectedAccounts();

    // TODO: move UpdateTypes into commands package, it's not used here anymore
    /**
     * The type of an {@link Update}.
     */
    enum UpdateType {
        /**
         * The type of updates that represent changes to an account
         */
        ACCOUNT_CHANGE,
        /**
         * The type of updates that represent changes to an usergroup
         */
        USERGROUP_CHANGE,
        /**
         * The type of updates that represent changes to an event
         */
        EVENT_CHANGE,
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
        REMOVE_CONTACT,

        ;

        @Override
        public String toString() {
            return this.name().toLowerCase().replace('_','-');
        }
    }

}
