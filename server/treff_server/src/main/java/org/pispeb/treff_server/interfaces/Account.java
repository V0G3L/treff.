package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.Position;

import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

/**
 * An object representing an account with an associated username, email address,
 * and password. An account may only be represented by a single {@link Account}
 * object at a time.
 */
public interface Account extends DataObject {

    /**
     * Returns the username of this account.
     **/
    String getUsername() throws DatabaseException;

    /**
     * Sets the username of this account.
     *
     * @param username The new username
     * @throws DuplicateUsernameException if another account is already
     *                                    associated with
     *                                    the supplied username. The username
     *                                    of this account is unchanged in
     *                                    this case.
     */
    void setUsername(String username) throws DuplicateUsernameException,
            DatabaseException;

    /**
     * Checks whether the supplied password matches the password of this
     * account.
     *
     * @param password The password to check
     * @return <code>true</code> if supplied password matches the password of
     * this account, <code>false</code> otherwise.
     */
    boolean checkPassword(String password) throws DatabaseException;

    /**
     * Sets the password of this account.
     *
     * @param password The new password
     */
    void setPassword(String password) throws DatabaseException;

    /**
     * Returns the email of this account.
     **/
    String getEmail() throws DatabaseException;

    /**
     * Sets the email address of this account.
     *
     * @param email The new email address
     * @throws DuplicateEmailException if another account is already
     *                                 associated with the
     *                                 supplied email address. The email
     *                                 address of this account is unchanged in
     *                                 this case.
     */
    void setEmail(String email) throws DuplicateEmailException,
            DatabaseException;

    /**
     * @return ID to Usergroup mapping containing only groups that this
     * account is a member
     * of.
     */
    Map<Integer, Usergroup> getAllGroups() throws DatabaseException;

    void addToGroup(Usergroup usergroup) throws DatabaseException;

    void removeFromGroup(Usergroup usergroup) throws DatabaseException;

    Position getLastPosition() throws DatabaseException;

    Date getLastPositionTime() throws DatabaseException;

    void updatePosition(Position position) throws DatabaseException;

    /**
     * Adds an {@link Update} that affects this {@link Account} to the set of
     * undelivered
     * Updates.
     * Will call {@link AccountUpdateListener#onUpdateAdded(Update)} on all
     * registered
     * AccountUpdateListeners.
     *
     * @param update The Update to add
     */
    void addUpdate(Update update) throws DatabaseException;

    /**
     * Returns the set of {@link Update}s that affect this {@link Account}
     * but have
     * not been delivered to it yet.
     *
     * @return The set of undelivered Updates, sorted by time in ascending
     * order.
     */
    SortedSet<Update> getUndeliveredUpdates() throws DatabaseException;

    /**
     * Marks an {@link Update} that affects this {@link Account} as
     * delivered, removing
     * it from the set returned by {@link #getUndeliveredUpdates()}.
     */
    void markUpdateAsDelivered(Update update) throws DatabaseException;

    /**
     * Registers an {@link AccountUpdateListener} whose
     * {@link AccountUpdateListener#onUpdateAdded(Update)}
     * method is called when an {@link Update} is added via
     * {@link #addUpdate(Update)}.
     *
     * @param updateListener The AccountUpdateListener to add
     */
    void addUpdateListener(AccountUpdateListener updateListener);

    /**
     * Removes an {@link AccountUpdateListener}, no longer notifying it when an
     * {@link Update} is added via {@link #addUpdate(Update)}.
     *
     * @param updateListener The AccountUpdateListener to remove
     */
    void removeUpdateListener(AccountUpdateListener updateListener);

    /**
     * Deletes this account. TODO: specify further
     */
    void delete() throws DatabaseException;

    // TODO: GetAffectedAccounts-method to all interfaces
    // Will return a Set<Account> of affected accounts when a setter is
    // invoked. Groups will return list of members. Accounts will return
    // list of members of all groups, contact list and blocked-by list.
    // All others will forward the call upwards until it hits Group.
}
