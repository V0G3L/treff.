package org.pispeb.treffpunkt.server.hibernate;

import org.apache.commons.codec.binary.Base64;
import org.pispeb.treffpunkt.server.ConfigKeys;
import org.pispeb.treffpunkt.server.PasswordHash;
import org.pispeb.treffpunkt.server.Position;
import org.pispeb.treffpunkt.server.exceptions.ContactRequestAlreadySentException;
import org.pispeb.treffpunkt.server.exceptions.ContactRequestNonexistantException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateEmailException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateUsernameException;
import org.pispeb.treffpunkt.server.interfaces.Account;
import org.pispeb.treffpunkt.server.interfaces.AccountUpdateListener;
import org.pispeb.treffpunkt.server.interfaces.Update;
import org.pispeb.treffpunkt.server.interfaces.Usergroup;

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

import static org.pispeb.treffpunkt.server.PasswordHash.generatePasswordHash;

@Entity
@Table(name = "accounts")
public class AccountHib extends DataObjectHib implements Account {

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
    private SortedSet<UpdateHib> undeliveredUpdates;

    @ManyToMany
    private Set<UsergroupHib> groups;

    @ManyToMany
    private Set<AccountHib> incomingRequests;
    @ManyToMany(mappedBy = "incomingRequests")
    private Set<AccountHib> outgoingRequests;

    // contacts need to be updated on both sides
    // no idea how to nicely implement symmetric reflexive relations in Hibernate
    @OneToMany
    private Set<AccountHib> contacts;
    @OneToMany
    private Set<AccountHib> blocks;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) throws DuplicateUsernameException {
        this.username = username;
    }

    @Override
    public boolean checkPassword(String password) {
        byte[] correctHash = generatePasswordHash(password,
                Base64.decodeBase64(passwortSalt))
                .hash;
        return Arrays.equals(Base64.decodeBase64(passwordHash), correctHash);
    }

    @Override
    public void setPassword(String password) {
        PasswordHash passwordHash
                = generatePasswordHash(password);
        // Store new salt and new hash in DB
        this.passwortSalt = Base64.encodeBase64String(passwordHash.salt);
        this.passwordHash = Base64.encodeBase64String(passwordHash.hash);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) throws DuplicateEmailException {
        this.email = email;
    }

    @Override
    public Map<Integer, UsergroupHib> getAllGroups() {
        return toMap(groups);
    }

    @Override
    public Usergroup createGroup(String name, Set<Account> members) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void sendContactRequest(Account receiver) {
        if (outgoingRequests.contains((AccountHib) receiver))
            throw new ContactRequestAlreadySentException();

        outgoingRequests.add((AccountHib) receiver);
    }

    @Override
    public void acceptContactRequest(Account sender) {
        AccountHib senderHib = (AccountHib) sender;
        if (!incomingRequests.contains(senderHib))
            throw new ContactRequestNonexistantException();

        incomingRequests.remove(senderHib);
        contacts.add(senderHib);
        senderHib.contacts.add(senderHib);
    }

    @Override
    public void rejectContactRequest(Account sender) {
        if (!incomingRequests.contains((AccountHib) sender))
            throw new ContactRequestNonexistantException();

        incomingRequests.remove(sender);
    }

    @Override
    public Map<Integer, ? extends Account> getAllIncomingContactRequests() {
        return toMap(incomingRequests);
    }

    @Override
    public Map<Integer, ? extends Account> getAllOutgoingContactRequests() {
        return toMap(outgoingRequests);
    }

    @Override
    public void removeContact(Account account) {
        AccountHib other = (AccountHib) account;
        contacts.remove(other);
        other.contacts.remove(this);
    }

    @Override
    public Map<Integer, ? extends Account> getAllContacts() {
        return toMap(contacts);
    }

    @Override
    public void addBlock(Account account) {
        this.blocks.add((AccountHib) account);
    }

    @Override
    public void removeBlock(Account account) {
        this.blocks.remove((AccountHib) account);
    }

    @Override
    public Map<Integer, ? extends Account> getAllBlocks() {
        return toMap(blocks);
    }

    @Override
    public Position getLastPosition() {
        return new Position(latitude, longitude);
    }

    @Override
    public Date getLastPositionTime() {
        return timeMeasured;
    }

    @Override
    public void updatePosition(Position position, Date timeMeasured) {
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.timeMeasured = timeMeasured;
    }

    @Override
    public String generateNewLoginToken() {
        int loginTokenSize = Integer.parseInt(
                config.getProperty(ConfigKeys.LOGIN_TOKEN_BYTES.toString()));
        SecureRandom random = new SecureRandom();
        byte[] loginTokenBytes = new byte[loginTokenSize];
        random.nextBytes(loginTokenBytes);

        this.loginToken = Base64.encodeBase64String(loginTokenBytes);
        return this.loginToken;
    }

    @Override
    public void invalidateLoginToken() {
        loginToken = null;
    }

    @Override
    public void addUpdate(Update update) {
        undeliveredUpdates.add((UpdateHib) update);
    }

    @Override
    public SortedSet<UpdateHib> getUndeliveredUpdates() {
        return undeliveredUpdates;
    }

    @Override
    public void markUpdateAsDelivered(Update update) {
        undeliveredUpdates.remove((UpdateHib) update);
    }

    @Override
    public void addUpdateListener(AccountUpdateListener updateListener) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void removeUpdateListener(AccountUpdateListener updateListener) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(Account o) {
        return this.getID() - o.getID();
    }
}
