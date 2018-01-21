package org.pispeb.treff_server.sql;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountUpdateListener;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.pispeb.treff_server.ConfigKeys.*;

public class AccountSQL extends SQLObject implements Account {

    private static final TableName TABLE_NAME = TableName.ACCOUNTS;

    private final Lock requestLock = new ReentrantLock();

    AccountSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
    }

    // TODO: write SQL statements

    @Override
    public String getUsername() throws DatabaseException {
        return (String) getProperties("username")
                .get("username");
    }

    @Override
    public void setUsername(String username) throws
            DuplicateUsernameException, DatabaseException {
        // have to acquire lock since username changes are not atomical
        EntityManagerSQL entityManager = EntityManagerSQL.getInstance();
        synchronized (entityManager.usernameLock) {
            // check for duplicates
            if (entityManager.hasAccountWithUsername(username)) {
                throw new DuplicateUsernameException();
            } else {
                setProperties(new AssignmentList()
                        .put("username", username));
            }
        }
    }

    private byte[] calculatePasswordHash(String password, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest
                    .getInstance(config.getProperty(
                            PASSWORD_HASH_ALG.toString()));
            messageDigest.update(
                    password.getBytes(Charset.forName("UTF-8")));
            messageDigest.update(salt);

            return messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AssertionError("Hash algorithm wasn't found" +
                    "even though it was used in SQLDatabase before. " +
                    "This really shouldn't happen.");
        }
    }

    @Override
    public boolean checkPassword(String password) throws DatabaseException {
        // Retrieve salt and hash from DB
        // Then check against hash(supplied password + salt)
        Map<String, Object> result = getProperties("passwordsalt",
                "passwordhash");

        byte[] correctHash = HexBin.decode(
                (String) result.get("passwordhash"));
        byte[] salt = HexBin.decode(
                (String) result.get("passwordsalt"));
        byte[] calculatedHash = calculatePasswordHash(password, salt);

        return Arrays.equals(calculatedHash, correctHash);
    }

    @Override
    public void setPassword(String password) throws DatabaseException {
        // Generate a new salt with a cryptographically strong RNG
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[
                Integer.parseInt(config.getProperty(
                        PASSWORD_SALT_BYTES.toString()))];
        secureRandom.nextBytes(salt);
        byte[] newHash = calculatePasswordHash(password, salt);

        // Store new salt and new hash in DB
        setProperties(new AssignmentList()
                .put("passwordsalt", HexBin.encode(salt))
                .put("passwordhash", HexBin.encode(newHash)));
    }

    @Override
    public String getEmail() throws DatabaseException {
        return (String) getProperties("email")
                .get("email");
    }

    @Override
    public void setEmail(String email) throws DuplicateEmailException,
            DatabaseException {
        // have to acquire lock since email address changes are not atomical
        EntityManagerSQL entityManager = EntityManagerSQL.getInstance();
        synchronized (entityManager.emailLock) {
            // check for duplicates
            if (entityManager.hasAccountWithEmail(email)) {
                throw new DuplicateUsernameException();
            } else {
                setProperties(new AssignmentList()
                        .put("email", email));
            }
        }
    }

    @Override
    public Map<Integer, Usergroup> getAllGroups() throws DatabaseException {
        return null;
    }

    @Override
    public void addToGroup(Usergroup usergroup) throws DatabaseException {

    }

    @Override
    public void removeFromGroup(Usergroup usergroup) throws DatabaseException {

    }

    @Override
    public Position getLastPosition() throws DatabaseException {
        return null;
    }

    @Override
    public Date getLastPositionTime() throws DatabaseException {
        return null;
    }

    @Override
    public void updatePosition(Position position) throws DatabaseException {
        // not persistent
    }

    @Override
    public void addUpdate(Update update) throws DatabaseException {

    }

    @Override
    public SortedSet<Update> getUndeliveredUpdates() throws DatabaseException {
        return null;
    }

    @Override
    public void markUpdateAsDelivered(Update update) throws DatabaseException {

    }

    @Override
    public void addUpdateListener(AccountUpdateListener updateListener) {

    }

    @Override
    public void removeUpdateListener(AccountUpdateListener updateListener) {

    }

    @Override
    public void delete() throws DatabaseException {
        // removes itself from all groups
        // removes all contacts (will also removed them from the other sides)
        // clears its own blocklist
        // clears itself from all other blocklists (somehow, unclear)
        // events and polls have to be able to handle non-existent creators
    }

    public Lock getRequestLock() {
        return requestLock;
    }

}
