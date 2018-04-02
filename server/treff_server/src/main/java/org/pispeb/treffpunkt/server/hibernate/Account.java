package org.pispeb.treffpunkt.server.hibernate;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Session;
import org.pispeb.treffpunkt.server.ConfigKeys;
import org.pispeb.treffpunkt.server.PasswordHash;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.exceptions.ContactRequestAlreadySentException;
import org.pispeb.treffpunkt.server.exceptions.ContactRequestNonexistantException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateEmailException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateUsernameException;
import org.pispeb.treffpunkt.server.interfaces.AccountUpdateListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static org.pispeb.treffpunkt.server.PasswordHash.generatePasswordHash;

/**
 * An object representing an account with an associated username, email address,
 * and password.
 */
@Entity
@Table(name = "accounts")
public class Account extends DataObject {

    @Column(nullable = false, unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String loginToken;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private String passwortSalt;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private Date timeMeasured;

    @OneToMany(orphanRemoval = true)
    @OrderBy("id ASC")
    private SortedSet<Update> undeliveredUpdates;

    @OneToMany(mappedBy = "account")
    private Set<GroupMembership> memberships;

    @ManyToMany
    private Set<Account> incomingRequests;
    @ManyToMany(mappedBy = "incomingRequests")
    private Set<Account> outgoingRequests;

    // contacts need to be updated on both sides
    // no idea how to nicely implement symmetric reflexive relations in Hibernate
    @OneToMany
    private Set<Account> contacts;
    @OneToMany
    private Set<Account> blocks;

    /**
     * Returns the username of this account.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The username of this account.
     **/
    public String getUsername() {
        return username;
    }

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
    public void setUsername(String username) throws DuplicateUsernameException {
        this.username = username;
    }

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
    public boolean checkPassword(String password) {
        byte[] correctHash = generatePasswordHash(password,
                Base64.decodeBase64(passwortSalt))
                .hash;
        return Arrays.equals(Base64.decodeBase64(passwordHash), correctHash);
    }

    /**
     * Sets the password of this account.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param password The new password
     */
    public void setPassword(String password) {
        PasswordHash passwordHash
                = generatePasswordHash(password);
        // Store new salt and new hash in DB
        this.passwortSalt = Base64.encodeBase64String(passwordHash.salt);
        this.passwordHash = Base64.encodeBase64String(passwordHash.hash);
    }

    /**
     * Returns the email address of this account.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The email address of this account.
     * If no email address has been set yet, this will return an empty string.
     **/
    public String getEmail() {
        return email;
    }

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
    public void setEmail(String email) throws DuplicateEmailException {
        this.email = email;
    }

    /**
     * Returns an unmodifiable [ID -> {@code Usergroup}] map holding all
     * {@code Usergroup}s that this {@code Account} is a member of.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return Map of {@code Usergroup} that this {@code Account} is a member of
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    public Map<Integer, Usergroup> getAllGroups() {
        Set<Usergroup> groups = memberships.stream()
                .map(GroupMembership::getUsergroup)
                .collect(Collectors.toSet());
        return toMap(groups);
    }

    /**
     * Creates a new {@code Usergroup} with the specified name and members.
     * This {@code Account} will be a member of the created group regardless of
     * whether it is contained in the specified set of members.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param name    The name of the group
     * @param members The members to be added in addition to this account
     * @return The created {@code Usergroup}
     */
    public Usergroup createGroup(String name, Set<Account> members, Session session) {
        members.add(this);

        Usergroup usergroup = new Usergroup();
        usergroup.setName(name);
        session.save(usergroup);
        members.forEach(m -> usergroup.addMember(m, session));
        return usergroup;
    }

    /**
     * Sends a contact request to the specified {@code Account}.
     * If the specified account has previously sent this {@code Account} a
     * contact request which is still pending, the contact request will
     * not be sent.
     * Instead, both accounts will be added as contacts.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param receiver The {@code Account} which to send the contact request to
     */
    public void sendContactRequest(Account receiver) {
        if (outgoingRequests.contains((Account) receiver))
            throw new ContactRequestAlreadySentException();

        outgoingRequests.add((Account) receiver);
    }

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
    public void acceptContactRequest(Account sender) {
        Account senderHib = (Account) sender;
        if (!incomingRequests.contains(senderHib))
            throw new ContactRequestNonexistantException();

        incomingRequests.remove(senderHib);
        contacts.add(senderHib);
        senderHib.contacts.add(senderHib);
    }

    /**
     * Rejects a contact request from the specified {@code Account} that was
     * previously sent to this {@code Account}.
     * This removes the contact request.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param sender The {@code Account} that sent the contact request
     */
    public void rejectContactRequest(Account sender) {
        if (!incomingRequests.contains((Account) sender))
            throw new ContactRequestNonexistantException();

        incomingRequests.remove(sender);
    }

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
    public Map<Integer, ? extends Account> getAllIncomingContactRequests() {
        return toMap(incomingRequests);
    }

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
    public Map<Integer, ? extends Account> getAllOutgoingContactRequests() {
        return toMap(outgoingRequests);
    }

    /**
     * Removes an {@code Account} from this {@code Account}'s contact list and
     * vice versa.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param account The account to be removed from the contact list
     */
    public void removeContact(Account account) {
        Account other = (Account) account;
        contacts.remove(other);
        other.contacts.remove(this);
    }

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
    public Map<Integer, ? extends Account> getAllContacts() {
        return toMap(contacts);
    }

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
    public void addBlock(Account account) {
        this.blocks.add((Account) account);
    }

    /**
     * Unblocks the specified {@code Account}, re-allowing it to send
     * contact requests to and receive them from this {@code Account}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param account The {@code Account} to unblock
     */
    public void removeBlock(Account account) {
        this.blocks.remove((Account) account);
    }

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
    public Map<Integer, ? extends Account> getAllBlocks() {
        return toMap(blocks);
    }

    /**
     * Returns the last position stored for this {@code Account}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The last position stored for this {@code Account} or null if
     * there is no stored position.
     */
    public Position getLastPosition() {
        return new Position(latitude, longitude);
    }

    /**
     * Returns the time at which the last position stored for this
     * {@code Account} was measured.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The time at which the last position stored for this
     * {@code Account} was measured or null if there is no stored position.
     */
    public Date getLastPositionTime() {
        return timeMeasured;
    }

    /**
     * Updates this {@link Account}'s position.
     * <p>
     * Requires a {@code WriteLock}.
     *
     * @param position     The position to update to
     * @param timeMeasured The time at which the specified position was
     *                     measured
     */
    public void updatePosition(Position position, Date timeMeasured) {
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.timeMeasured = timeMeasured;
    }

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
    public String generateNewLoginToken() {
        int loginTokenSize = Integer.parseInt(
                config.getProperty(ConfigKeys.LOGIN_TOKEN_BYTES.toString()));
        SecureRandom random = new SecureRandom();
        byte[] loginTokenBytes = new byte[loginTokenSize];
        random.nextBytes(loginTokenBytes);

        this.loginToken = Base64.encodeBase64String(loginTokenBytes);
        return this.loginToken;
    }

    /**
     * Invalidates this {@code Account}s current login token
     * <p>
     * Requires a {@code ReadLock}.
     */
    public void invalidateLoginToken() {
        loginToken = null;
    }

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
    public void addUpdate(Update update) {
        undeliveredUpdates.add((Update) update);
    }

    /**
     * Returns the set of {@code Update}s that affect this {@code Account}
     * but have not been delivered to it's user yet.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @return The set of undelivered {@code Update}s, sorted in ascending
     * order of their creation time.
     */
    public SortedSet<Update> getUndeliveredUpdates() {
        return undeliveredUpdates;
    }

    /**
     * Marks an {@code Update} that affects this {@code Account} as
     * delivered, removing it from the set returned by
     * {@link #getUndeliveredUpdates()}.
     * <p>
     * Requires a {@code ReadLock}.
     */
    public void markUpdateAsDelivered(Update update) {
        undeliveredUpdates.remove((Update) update);
    }

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
    public void addUpdateListener(AccountUpdateListener updateListener) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    /**
     * Removes an {@link AccountUpdateListener}, no longer notifying it when an
     * {@link Update} is added via {@link #addUpdate(Update)}.
     * <p>
     * Requires a {@code ReadLock}.
     *
     * @param updateListener The AccountUpdateListener to remove
     */
    public void removeUpdateListener(AccountUpdateListener updateListener) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

}
