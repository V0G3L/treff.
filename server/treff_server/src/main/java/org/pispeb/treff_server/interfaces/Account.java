package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.Position;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * An object representing an account with an associated username, email address,
 * and password. An account may only be represented by a single {@link Account}
 * object at a time.
 */
public interface Account extends DataObject, Comparable<Account> {

    /**
     * Returns the username of this account.
     **/
    String getUsername();

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
    void setUsername(String username) throws DuplicateUsernameException;

    /**
     * Checks whether the supplied password matches the password of this
     * account.
     *
     * @param password The password to check
     * @return <code>true</code> if supplied password matches the password of
     * this account, <code>false</code> otherwise.
     */
    boolean checkPassword(String password);

    /**
     * Sets the password of this account.
     *
     * @param password The new password
     */
    void setPassword(String password);

    /**
     * Returns the email of this account.
     **/
    String getEmail();

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
    void setEmail(String email) throws DuplicateEmailException;

    // TODO: document unmodifiability everywhere

    /**
     * Returns an unmodifiable ID -> {@code Usergroup} map holding all
     * usergroups that this {@code Account} is a member of.
     * @return Map of {@code Usergroup} that this {@code Account} is a member of
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Usergroup> getAllGroups();

    /**
     * Creates a new {@code Usergroup} with the specified name and members.
     * This {@code Account} will be a member of the created group regardless of
     * whether it is contained in the specified set of members.
     *
     * @param name The name of the group
     * @param members The members to be added in addition to this account
     * @return The created {@code Usergroup}
     */
    Usergroup createGroup(String name, Set<Account> members);

    void addToGroup(Usergroup usergroup);

    void removeFromGroup(Usergroup usergroup);

    void sendContactRequest(Account receiver);

    void acceptContactRequest(Account sender);

    void rejectContactRequest(Account sender);

    Map<Integer, Account> getAllIncomingContactRequests();
    Map<Integer, Account> getAllOutgoingContactRequests();

    void removeContact(Account account);

    Map<Integer, Account> getAllContacts();

    void addBlock(Account account);

    void removeBlock(Account account);

    Map<Integer, Account> getAllBlocks();

    Position getLastPosition();

    Date getLastPositionTime();

    void updatePosition(Position position, Date timeMeasured);

    String generateNewLoginToken();

    void invalidateLoginToken();

    /**
     * Adds an {@code Update} to this {@code Account}'s set of
     * undelivered Updates.
     * Will call {@link AccountUpdateListener#onUpdateAdded(Update)} on all
     * registered AccountUpdateListeners.
     *
     * @param update The UpdateToSerialize to add
     */
    void addUpdate(Update update);

    /**
     * Returns the set of {@link Update}s that affect this {@link Account}
     * but have not been delivered to it's user yet.
     *
     * @return The set of undelivered Updates, sorted by time in ascending
     * order.
     */
    SortedSet<Update> getUndeliveredUpdates();

    /**
     * Marks an {@link Update} that affects this {@link Account} as
     * delivered, removing it from the set returned by
     * {@link #getUndeliveredUpdates()}.
     */
    void markUpdateAsDelivered(Update update);

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
    void delete();

    // TODO: GetAffectedAccounts-method to all interfaces
    // Will return a Set<Account> of affected accounts when a setter is
    // invoked. Groups will return list of members. Accounts will return
    // list of members of all groups, contact list and blocked-by list.
    // All others will forward the call upwards until it hits Group.
}
