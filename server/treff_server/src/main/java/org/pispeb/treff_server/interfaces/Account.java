package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.sql.AccountSQL;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * An object representing an account with an associated username, email address,
 * and password.
 */
public interface Account extends DataObject, Comparable<Account> {

    /**
     * Returns the username of this account.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The username of this account.
     **/
    String getUsername();

    /**
     * Sets the username of this account.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param username The new username
     * @throws DuplicateUsernameException if another account is already
     *                                    associated with
     *                                    the supplied username.
     *                                    The username of this account remains
     *                                    unchanged in this case.
     */
    void setUsername(String username) throws DuplicateUsernameException;

    /**
     * Checks whether the supplied password matches the password of this
     * account.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param password The password to check
     * @return <code>true</code> if and only if the supplied password matches
     * the password of this account.
     */
    boolean checkPassword(String password);

    /**
     * Sets the password of this account.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param password The new password
     */
    void setPassword(String password);

    /**
     * Returns the email address of this account.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The email address of this account.
     * If no email address has been set yet, this will return an empty string.
     **/
    String getEmail();

    /**
     * Sets the email address of this account.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param email The new email address
     * @throws DuplicateEmailException if another account is already
     *                                 associated with the
     *                                 supplied email address.
     *                                 The email address of this account remains
     *                                 unchanged in this case.
     */
    void setEmail(String email) throws DuplicateEmailException;

    /**
     * Returns an unmodifiable [ID -> {@code Usergroup}] map holding all
     * {@code Usergroup}s that this {@code Account} is a member of.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Usergroup} that this {@code Account} is a member of
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Usergroup> getAllGroups();

    /**
     * Creates a new {@code Usergroup} with the specified name and members.
     * This {@code Account} will be a member of the created group regardless of
     * whether it is contained in the specified set of members.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param name The name of the group
     * @param members The members to be added in addition to this account
     * @return The created {@code Usergroup}
     */
    Usergroup createGroup(String name, Set<Account> members);

    /**
     * Sends a contact request to the specified {@code Account}.
     * If the specified account has previously sent this {@code Account} a
     * contact request which is still pending, the contact request will
     * not be sent.
     * Instead, both accounts will be added as contacts.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param receiver The {@code Account} which to send the contact request to
     */
    void sendContactRequest(Account receiver);

    /**
     * Accepts a contact request from the specified {@code Account} that was
     * previously sent to this {@code Account}.
     * This adds both {@code Account}s to each other's contact list and removes
     * the contact request.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param sender The {@code Account} that sent the contact request
     */
    void acceptContactRequest(Account sender);

    /**
     * Rejects a contact request from the specified {@code Account} that was
     * previously sent to this {@code Account}.
     * This removes the contact request.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param sender The {@code Account} that sent the contact request
     */
    void rejectContactRequest(Account sender);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s which have sent a contact request to this
     * {@code Account} that has not yet been accepted or rejected.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Unmodifiable [ID -> {@code Account}] map
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllIncomingContactRequests();

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s which have received a contact request from this
     * {@code Account} that has not yet been accepted or rejected.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Unmodifiable [ID -> {@code Account}] map
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllOutgoingContactRequests();

    /**
     * Removes an {@code Account} from this {@code Account}'s contact list and
     * vice versa.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param account The account to be removed from the contact list
     */
    void removeContact(Account account);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s which are on the contact list of this
     * {@code Account}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Unmodifiable [ID -> {@code Account}] map
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllContacts();


    /**
     * Blocks the specified {@code Account}, preventing it from sending
     * contact requests to or receiving them from this {@code Account}.
     * If the two {@code Account}s are on each other's contact list, they are
     * also removed from those.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param account The {@code Account} to block
     */
    void addBlock(Account account);

    /**
     * Unblocks the specified {@code Account}, re-allowing it to send
     * contact requests to and receive them from this {@code Account}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param account The {@code Account} to unblock
     */
    void removeBlock(Account account);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s which are currently being blocked by this
     * {@code Account}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Unmodifiable [ID -> {@code Account}] map
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllBlocks();

    /**
     * Returns the last position stored for this {@code Account}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The last position stored for this {@code Account} or null if
     * there is no stored position.
     */
    Position getLastPosition();

    /**
     * Returns the time at which the last position stored for this
     * {@code Account} was measured.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The time at which the last position stored for this
     * {@code Account} was measured or null if there is no stored position.
     */
    Date getLastPositionTime();

    /**
     * Updates this {@link Account}'s position.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position The position to update to
     * @param timeMeasured The time at which the specified position was
     *                     measured
     */
    void updatePosition(Position position, Date timeMeasured);

    /**
     * Generates a new login token for this {@code Account} that can be used
     * to authenticate this {@code Account}'s user.
     * If a login token was generated for this {@code Account} before, that old
     * token is invalidated.
     * If no valid login token for this {@code Account} exists, an empty string
     * is returned.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The newly generated login token
     */
    String generateNewLoginToken();

    /**
     * Invalidates this {@code Account}s current login token
     * <p>
     * Requires a {@code ReadLock}.
     */
    void invalidateLoginToken();

    /**
     * Adds an {@code Update} to this {@code Account}'s set of
     * undelivered Updates.
     * Will call {@link AccountUpdateListener#onUpdateAdded(Update)} on all
     * registered AccountUpdateListeners.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param update The {@code Update} to add
     */
    void addUpdate(Update update);

    /**
     * Returns the set of {@code Update}s that affect this {@code Account}
     * but have not been delivered to it's user yet.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The set of undelivered {@code Update}s, sorted in ascending
     * order of their creation time.
     */
    SortedSet<? extends Update> getUndeliveredUpdates();

    /**
     * Marks an {@code Update} that affects this {@code Account} as
     * delivered, removing it from the set returned by
     * {@link #getUndeliveredUpdates()}.
     * <p>
     * Requires a {@code ReadLock}.
     */
    void markUpdateAsDelivered(Update update);

    /**
     * Registers an {@link AccountUpdateListener} whose
     * {@link AccountUpdateListener#onUpdateAdded(Update)}
     * method is called when an {@link Update} is added via
     * {@link #addUpdate(Update)}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param updateListener The AccountUpdateListener to add
     */
    void addUpdateListener(AccountUpdateListener updateListener);

    /**
     * Removes an {@link AccountUpdateListener}, no longer notifying it when an
     * {@link Update} is added via {@link #addUpdate(Update)}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param updateListener The AccountUpdateListener to remove
     */
    void removeUpdateListener(AccountUpdateListener updateListener);

    /**
     * Deletes this account.
     * This has, in addition to the effects detailed in {@link DataObject}, the
     * following consequences:
     * <ul>
     *     <li>removes participation status of this account from all
     *     active events</li>
     *     <li>removes votes made by this account from all active poll
     *     options</li>
     *     <li>removes this account from all groups</li>
     *     <li>removes all contacts of this account</li>
     *     <li>cancels all contact requests made by this account</li>
     *     <li>rejects all contact request for this account</li>
     *     <li>removes all blocks made by or against this account</li>
     *     <li>invalidates this account's login token</li>
     * </ul>
     *
     * Requires a {@code WriteLock} on this {@code Account},
     * {@code WriteLock}s on all of this {@code Account}'s groups,
     * and {@code ReadLock}s on all of this {@code Account}'s contacts and all
     * {@code Account}s that have sent or received a still pending contact
     * request to/from this {@code Account}.
     */
    void delete();

    // Possible redesign: GetAffectedAccounts-method on all interfaces
    // Will return a Set<Account> of affected accounts when a setter is
    // invoked. Groups will return list of members. Accounts will return
    // list of members of all groups, contact list and blocked-by list.
    // All others will forward the call upwards until it hits Group.
}
