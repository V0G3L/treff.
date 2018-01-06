package org.pispeb.treff_server.interfaces;

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
public interface Account {
    /** Returns the username of this account. **/
    String getUsername();

    /** Sets the username of this account.
     * @param username The new username
     * @throws DuplicateUsernameException if another account is already associated with
     * the supplied username. The username of this account is unchanged in
     * this case. */
    void setUsername(String username) throws DuplicateUsernameException;

    /** Checks whether the supplied password matches the password of this account.
     * @param password The password to check
     * @return <code>true</code> if supplied password matches the password of
     * this account, <code>false</code> otherwise. */
    boolean checkPassword(String password);

    /** Sets the password of this account.
     * @param password The new password */
    void setPassword(String password);

    /** Returns the email of this account. **/
    String getEmail();

    /** Sets the email address of this account.
     * @param email The new email address
     * @throws DuplicateEmailException if another account is already associated with the
     * supplied email address. The email address of this account is unchanged in
     * this case. */
    void setEmail(String email) throws DuplicateEmailException;

    /**
     *
     * @return ID -> Usergroup mapping containing only groups that this account is a member
     * of.
     */
    Map<Integer, Usergroup> getAllGroups();
    void addToGroup(Usergroup usergroup);
    void removeFromGroup(Usergroup usergroup);

    Position getLastPosition();
    Date getLastPositionTime();

    void updatePosition(Position position);

    /**
     * Adds an {@link Update} that affects this {@link Account} to the set of undelivered
     * Updates.
     * Will call {@link AccountUpdateListener#onUpdateAdded(Update)} on all registered
     * AccountUpdateListeners.
     * @param update The Update to add
     */
    void addUpdate(Update update);

    /**
     * Returns the set of {@link Update}s that affect this {@link Account} but have
     * not been delivered to it yet.
     * @return The set of undelivered Updates, sorted by time in ascending order.
     */
    SortedSet<Update> getUndeliveredUpdates();

    /**
     * Marks an {@link Update} that affects this {@link Account} as delivered, removing
     * it from the set returned by {@link #getUndeliveredUpdates()}.
     */
    void markUpdateAsDelivered(Update update);

    /**
     * Registers an {@link AccountUpdateListener} whose
     * {@link AccountUpdateListener#onUpdateAdded(Update)}
     * method is called when an {@link Update} is added via {@link #addUpdate(Update)}.
     * @param updateListener The AccountUpdateListener to add
     */
    void addUpdateListener(AccountUpdateListener updateListener);

    /**
     * Removes an {@link AccountUpdateListener}, no longer notifying it when an
     * {@link Update} is added via {@link #addUpdate(Update)}.
     * @param updateListener The AccountUpdateListener to remove
     */
    void removeUpdateListener(AccountUpdateListener updateListener);

    /** Deletes this account. TODO: specify further
     */
    void delete();
}
