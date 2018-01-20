package org.pispeb.treff_server.interfaces;


import org.pispeb.treff_server.exceptions.DatabaseException;

import javax.json.JsonObject;
import java.util.Date;
import java.util.Set;

/**
 * Represents a change to the database caused by user interaction that
 * affects other {@link Account}s.
 * This change can be either an edit to an entity or a chat message sent to a
 * {@link Usergroup}.
 * <p>
 * <p>Once an {@link Update} is sent to a client device using an {@link Account}
 * that is affected by that Update, the Account should be removed from the
 * Update's set of affected Accounts to ensure that the same Account isn't
 * handed
 * the same Update twice.
 * An Update that has no more affected Accounts should be deleted and no longer
 * referenced.</p>
 * <p>
 * <p>Updates are naturally ordered from oldest to newest.</p>
 */
public interface Update extends Comparable<Update> {

    /**
     * Returns the time the {@link Update} was created, used for ordering
     *
     * @return The time the Update was created
     */
    Date getTime() throws DatabaseException;

    UpdateType getType() throws DatabaseException;

    /**
     * Returns the content of the Update.
     *
     * @return A {@link JsonObject} containing
     * all necessary IDs used for referencing in the
     * communication protocol, and either the names of the edited fields and
     * their
     * values or the chat message.
     * <p>The structure of the JsonObject depends on the type.
     * For an {@link UpdateType#EDIT} Update, the format is a partial extensive
     * description omitting all non-id fields that didn't change.</p>
     * <p>For a {@link UpdateType#CHAT} Update, the format is as follows:</p>
     * <code>{ "group-id": id-of-group, "message": the-message }</code>
     */
    JsonObject getUpdate() throws DatabaseException;

    /**
     * Adds an {@link Account} to the set of affected Accounts.
     * Duplicate additions will be ignored.
     *
     * @param account The Account to be added
     */
    void addAffectedAccount(Account account) throws DatabaseException;

    /**
     * Removes an {@link Account} from the set of affected Accounts.
     * Should the set be empty after the removal, this method will
     * return true.
     * In that case, the Update should be considered as deleted.
     * It's methods should no longer be used and all references to it
     * should be removed.
     *
     * @param account The Account to be removed
     * @return <code>true</code> if the set of affected Accounts is
     * empty after removal, <code>false</code> otherwise.
     */
    boolean removeAffectedAccount(Account account) throws DatabaseException;

    /**
     * Returns the set of affected {@link Account}.
     *
     * @return The set of affected Accounts
     */
    Set<Account> getAffectedAccounts() throws DatabaseException;

    /**
     * The type of an {@link Update}.
     */
    enum UpdateType {
        /**
         * The type of Updates that represent changes to the fields
         * of database entities.
         */
        EDIT("edit"),
        /**
         * The type of Updates that represent chat messages sent to
         * {@link Usergroup}s
         */
        CHAT("chat");

        private final String name;

        UpdateType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
