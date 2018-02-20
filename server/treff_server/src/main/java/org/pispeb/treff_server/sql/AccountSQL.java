package org.pispeb.treff_server.sql;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.pispeb.treff_server.ConfigKeys;
import org.pispeb.treff_server.PasswordHash;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.AccountNotInGroupException;
import org.pispeb.treff_server.exceptions.ContactRequestNonexistantException;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountUpdateListener;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;
import org.pispeb.treff_server.sql.resultsethandler.DataObjectHandler;
import org.pispeb.treff_server.sql.resultsethandler.DataObjectMapHandler;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.pispeb.treff_server.ConfigKeys.*;

public class AccountSQL extends SQLObject implements Account {

    private static final TableName TABLE_NAME = TableName.ACCOUNTS;
    private final Lock requestLock = new ReentrantLock();
    private Set<AccountUpdateListener> listeners = new HashSet<>();

    AccountSQL(int id, SQLDatabase database,
               EntityManagerSQL entityManager, Properties config) {
        super(id, TABLE_NAME, database, entityManager, config);
    }

    @Override
    public String getUsername() {
        return (String) getProperties("username").get("username");
    }

    @Override
    public void setUsername(String username) throws
            DuplicateUsernameException {
        // have to acquire lock since username changes are not atomical
        synchronized (entityManager.usernameLock) {
            // check for duplicates
            if (entityManager.usernameAvailable(username)) {
                throw new DuplicateUsernameException();
            } else {
                setProperties(new AssignmentList()
                        .put("username", username));
            }
        }
    }

    static PasswordHash generatePasswordHash(String password, String hashAlg,
                                             int saltBytes) {
        // Generate a new salt with a cryptographically strong RNG
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[saltBytes];
        secureRandom.nextBytes(salt);
        return generatePasswordHash(password, hashAlg, salt);
    }

    private static PasswordHash generatePasswordHash(String password,
                                                     String hashAlg,
                                                     byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest
                    .getInstance(hashAlg);
            messageDigest.update(
                    password.getBytes(Charset.forName("UTF-8")));
            messageDigest.update(salt);

            return new PasswordHash(messageDigest.digest(), salt);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AssertionError("Hash algorithm wasn't found" +
                    "even though the configuration parameters where checked " +
                    "at startup. This really shouldn't happen.");
        }
    }

    @Override
    public boolean checkPassword(String password) {
        // Retrieve salt and hash from DB
        // Then check against hash(supplied password + salt)
        Map<String, Object> result = getProperties("passwordsalt",
                "passwordhash");

        PasswordHash correctHash;
        try {
            correctHash = new PasswordHash(
                    Hex.decodeHex(
                            (String) result.get("passwordhash")),
                    Hex.decodeHex(
                            (String) result.get("passwordsalt")));
        } catch (DecoderException e) {
            throw new DatabaseException(e);
        }
        String hashAlg = config.getProperty(PASSWORD_HASH_ALG.toString());
        PasswordHash calculatedHash
                = generatePasswordHash(password, hashAlg, correctHash.salt);

        return Arrays.equals(calculatedHash.hash, correctHash.hash);
    }

    @Override
    public void setPassword(String password) {
        String hashAlg = config.getProperty(PASSWORD_HASH_ALG.toString());
        int saltBytes = Integer.parseInt(config.getProperty(
                PASSWORD_SALT_BYTES.toString()));
        PasswordHash passwordHash
                = generatePasswordHash(password, hashAlg, saltBytes);
        // Store new salt and new hash in DB
        setProperties(new AssignmentList()
                .put("passwordsalt", passwordHash.getSaltAsHex())
                .put("passwordhash", passwordHash.getHashAsHex()));
    }

    @Override
    public String getEmail() {
        return (String) getProperties("email").get("email");
    }

    @Override
    public void setEmail(String email) throws DuplicateEmailException {
        // have to acquire lock since email address changes are not atomical
        synchronized (entityManager.emailLock) {
            // check for duplicates
            if (entityManager.emailAvailable(email)) {
                throw new DuplicateUsernameException();
            } else {
                setProperties(new AssignmentList()
                        .put("email", email));
            }
        }
    }

    @Override
    public Map<Integer, UsergroupSQL> getAllGroups() {
        return Collections.unmodifiableMap(database.query(
                "SELECT usergroupid FROM %s WHERE accountid=?;",
                TableName.GROUPMEMBERSHIPS,
                new DataObjectMapHandler<>(UsergroupSQL.class, entityManager),
                id));
    }

    @Override
    public Usergroup createGroup(String name, Set<Account> members) {
        // Insert empty group into database and get a reference on it
        UsergroupSQL usergroup = database.insert(
                "INSERT INTO %s(name) VALUES (?);",
                TableName.USERGROUPS,
                new DataObjectHandler<>(UsergroupSQL.class, entityManager),
                name);

        // Add all members
        // make sure this account also gets added
        usergroup.getReadWriteLock().writeLock().lock();
        try {
            members.add(this);
            members.forEach(usergroup::addMember);
            return usergroup;
        } finally {
            usergroup.getReadWriteLock().writeLock().unlock();
        }
    }

    @Override
    public void sendContactRequest(Account receiver) {
        // if existing sent contact request is still pending,
        // don't do anything
        if (this.getAllOutgoingContactRequests()
                .containsKey(receiver.getID()))
            return;
        // if this received a contact request from receiver, accept that
        // one instead and return
        if (this.getAllIncomingContactRequests()
                .containsKey(receiver.getID())) {
            acceptContactRequest(receiver);
            return;
        }

        database.insert(
                "INSERT INTO %s(sender,receiver) VALUES (?,?);",
                TableName.CONTACTREQUESTS,
                rs -> null,
                this.id,
                receiver.getID());
    }

    private void deleteContactRequestFrom(Account sender) {
        if (!this.getAllIncomingContactRequests().containsKey(sender.getID()))
            throw new ContactRequestNonexistantException();

        // delete contact request
        database.update(
                "DELETE FROM %s WHERE sender=? AND receiver=?;",
                TableName.CONTACTREQUESTS,
                sender.getID(),
                this.id);

    }

    @Override
    public void acceptContactRequest(Account sender) {
        // delete contact request, then add to contacts
        this.deleteContactRequestFrom(sender);
        this.addContact(sender);
    }

    @Override
    public void rejectContactRequest(Account sender) {
        this.deleteContactRequestFrom(sender);
    }

    @Override
    public Map<Integer, Account> getAllIncomingContactRequests() {
        return database.query(
                "SELECT sender FROM %s WHERE receiver=?;",
                TableName.CONTACTREQUESTS,
                new ColumnListHandler<Integer>(),
                this.id)
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        entityManager::getAccount));
    }

    @Override
    public Map<Integer, Account> getAllOutgoingContactRequests() {
        return database.query(
                "SELECT receiver FROM %s WHERE sender=?;",
                TableName.CONTACTREQUESTS,
                new ColumnListHandler<Integer>(),
                this.id)
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        entityManager::getAccount));
    }

    private void addContact(Account account) {
        int lowID = Math.min(this.id, account.getID());
        int highID = Math.max(this.id, account.getID());

        database.insert(
                "INSERT INTO %s(lowid,highid) VALUES (?,?);",
                TableName.CONTACTS,
                (rs -> null),
                lowID,
                highID);
    }

    @Override
    public void removeContact(Account account) {
        int lowID = Math.min(this.id, account.getID());
        int highID = Math.max(this.id, account.getID());

        database.update(
                "DELETE FROM %s WHERE lowid=? AND highid=?;",
                TableName.CONTACTS,
                lowID,
                highID);
    }

    @Override
    public Map<Integer, Account> getAllContacts() {
        // get ID list
        return database
                // get all contact relations that this account is a part of
                .query(
                        "SELECT (lowid,highid) FROM %s WHERE lowid=? " +
                                "OR highid=?;",
                        TableName.CONTACTS,
                        new MapListHandler(),
                        id,
                        id)
                .stream()
                // map to id of other account
                .map((rsMap) -> (this.id == (Integer) rsMap.get("lowid"))
                        ? (Integer) rsMap.get("highid")
                        : (Integer) rsMap.get("lowid"))
                // create ID -> AccountSQL map
                .collect(Collectors.toMap(Function.identity(),
                        entityManager::getAccount));
    }

    @Override
    public void addBlock(Account account) {
        database.insert(
                "INSERT INTO %s(blocker,blocked) VALUES (?,?);",
                TableName.BLOCKS,
                (rs -> null),
                this.id,
                account.getID());
    }

    @Override
    public void removeBlock(Account account) {
        database.update(
                "DELETE FROM %s WHERE blocker=? AND blocked=?;",
                TableName.BLOCKS,
                this.id,
                account.getID());
    }

    @Override
    public Map<Integer, Account> getAllBlocks() {
        // get ID list
        return database.query(
                "SELECT blocked FROM %s WHERE blocker=?;",
                TableName.BLOCKS,
                new ColumnListHandler<Integer>(),
                id)
                .stream()
                // create ID -> AccountSQL map
                .collect(Collectors.toMap(Function.identity(),
                        entityManager::getAccount));
    }

    @Override
    public Position getLastPosition() {
        Map<String, Object> properties
                = getProperties("latitude", "longitude");
        return new Position(
                (Double) properties.get("latitude"),
                (Double) properties.get("longitude"));
    }

    @Override
    public Date getLastPositionTime() {
        return (Date) getProperties("timemeasured").get("timemeasured");
    }

    @Override
    public void updatePosition(Position position, Date timeMeasured) {
        setProperties(new AssignmentList()
                .put("latitude", position.latitude)
                .put("longitude", position.longitude)
                .put("timemeasured", timeMeasured));
    }

    @Override
    public String generateNewLoginToken() {
        SecureRandom secureRandom = new SecureRandom();

        int loginTokenByteSize = Integer.parseInt(config.getProperty(
                ConfigKeys.LOGIN_TOKEN_BYTES.toString()));
        byte[] loginTokenBytes = new byte[loginTokenByteSize];
        secureRandom.nextBytes(loginTokenBytes);
        String loginToken = Hex.encodeHexString(loginTokenBytes);
        database.update(
                "UPDATE %s SET logintoken=? WHERE id=?;",
                TableName.ACCOUNTS,
                loginToken,
                id);
        return loginToken;
    }

    @Override
    public void invalidateLoginToken() {
        database.update(
                "UPDATE %s SET logintoken=NULL WHERE id=?;",
                TableName.ACCOUNTS,
                id);
    }

    @Override
    public void addUpdate(Update update) {
        database.insert(
                "INSERT INTO %s(updateid,accountid) VALUES (?,?);",
                TableName.UPDATEAFFECTIONS,
                (rs -> null),
                update.getID(),
                this.id);
        // inform updatelisteners asynchronously
        listeners.forEach(
                l -> new Thread(() -> l.onUpdateAdded(update)).start());
    }

    @Override
    public SortedSet<Update> getUndeliveredUpdates() {
        Set<Integer> updateIDs;
        updateIDs = database.query(
                "SELECT updateid FROM %s WHERE accountid=?;",
                TableName.UPDATEAFFECTIONS,
                rs -> {
                    Set<Integer> ids = new HashSet<>();
                    while (rs.next())
                        ids.add(rs.getInt(1));
                    return ids;
                },
                this.id
        );
        return updateIDs.stream()
                .map(entityManager::getUpdate)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void markUpdateAsDelivered(Update update) {
        database.update(
                "DELETE FROM %s WHERE accountid=? AND updateid=?;",
                TableName.UPDATEAFFECTIONS,
                this.id,
                update.getID());
    }

    @Override
    public void addUpdateListener(AccountUpdateListener updateListener) {
        listeners.add(updateListener);
    }

    @Override
    public void removeUpdateListener(AccountUpdateListener updateListener) {
        listeners.remove(updateListener);
    }

    @Override
    public void delete() {
        // remove this from all groups
        // it's a group's responsibility to remove the user from all
        // events and polls
        SortedSet<Usergroup> groups
                = new TreeSet<>(this.getAllGroups().values());

        groups.forEach(g -> g.getReadWriteLock().writeLock().lock());
        try {
            groups.forEach(g -> g.removeMember(this));
        } finally {
            groups.forEach(g -> g.getReadWriteLock().writeLock().unlock());
        }

        // removes all contacts (will also removed them from the other sides)
        SortedSet<Account> contacts
                = new TreeSet<>(this.getAllContacts().values());

        contacts.forEach(a -> a.getReadWriteLock().writeLock().lock());
        try {
            contacts.forEach(this::removeContact);
        } finally {
            contacts.forEach(a -> a.getReadWriteLock().writeLock().unlock());
        }

        // clears its own blocklist
        this.getAllBlocks().values().forEach(this::removeBlock);

        // clears itself from all other blocklists
        database.update(
                "DELETE FROM %s WHERE blocked=?;",
                TableName.BLOCKS,
                this.id);

        // set deleted flag, signalling commands that still have a reference
        // on this object to no longer use it
        deleted = true;

        // events and polls have to be able to handle non-existent creators
    }

    public Lock getRequestLock() {
        return requestLock;
    }

    @Override
    public int compareTo(Account o) {
        return this.id - o.getID();
    }
}
