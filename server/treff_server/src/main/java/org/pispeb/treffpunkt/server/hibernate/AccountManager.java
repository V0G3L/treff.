package org.pispeb.treffpunkt.server.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.Session;
import org.pispeb.treffpunkt.server.Server;
import org.pispeb.treffpunkt.server.commands.updates.UpdateToSerialize;
import org.pispeb.treffpunkt.server.exceptions.DuplicateUsernameException;
import org.pispeb.treffpunkt.server.exceptions.ProgrammingException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

/**
 * An interface for an Hibernate database that allows retrieval and
 * creation of {@link Account}s and creation of {@link Update}s.
 * TODO: specify session dependency
 * The user of this interface can supply a username or an email address to
 * retrieve an existing account, represented by an {@link Account} object.
 */
public class AccountManager {

    private final Session session;
    private final CriteriaBuilder cb;

    public AccountManager(Session session) {
        this.session = session;
        this.cb = session.getCriteriaBuilder();
    }

    private <T extends DataObject> T getByUniqueField(Class<T> clazz, String fieldName,
                                                      Object expectedValue) {
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> from = query.from(clazz);
        query.select(from)
                .where(cb.equal(from.get(fieldName), expectedValue));
        return session.createQuery(query).uniqueResult();
    }

    /**
     * Returns the account that is associated with the given username.
     *
     * @param username The username of the account
     * @return An {@link Account} object representing the account with the
     * supplied username or <code>null</code> if no such account exists.
     */
    public Account getAccountByUsername(String username) {
        return getByUniqueField(Account.class, "username", username);
    }

    /**
     * Returns the account that is associated with the given email address.
     *
     * @param email The email address of the account
     * @return An {@link Account} object representing the account with the
     * supplied email address or <code>null</code> if no such account exists.
     */
    public Account getAccountByEmail(String email) {
        return getByUniqueField(Account.class, "email", email);
    }

    /**
     * Returns the account that is associated with the given ID.
     *
     * @param id The ID of the account
     * @return An {@link Account} object representing the account with the
     * supplied ID or <code>null</code> if no such account exists.
     */
    public Account getAccount(int id) {
        return session.get(Account.class, id);
    }

    /**
     * Creates a new account with the supplied username and password.
     *
     * @param username The username for the new account
     * @param password The password of the new account
     * @return The created {@link Account}
     * @throws DuplicateUsernameException if the supplied username is already
     *                                    associated with another account.
     *                                    No account will be
     *                                    created in this case.
     */
    public Account createAccount(String username, String password)
            throws DuplicateUsernameException {
        if (getAccountByUsername(username) != null)
            throw new DuplicateUsernameException();

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        session.save(account);
        return account;
    }

    /**
     * Returns the account that is associated with the given login token.
     *
     * @param token The login token of the account
     * @return An {@link Account} object representing the account with the
     * supplied login token or <code>null</code> if no such account exists.
     */
    public Account getAccountByLoginToken(String token) {
        return getByUniqueField(Account.class, "loginToken", token);
    }

    /**
     * Create a new {@code Update} with the supplied content and the supplied
     * creation time that affects the supplied set of {@code Account}s.
     * The {@code Update} will automatically be added to the set of undelivered
     * updates of each supplied affected {@code Account}.
     * <p>
     * Note that it is assumed the the supplied creation time is also encoded
     * into the update content.
     * <p>
     * Requires the {@code ReadLock} of all affected {@link Account}s to be
     * held.
     *
     * @param updateContent    The content of the {@code Update} in the format
     *                         specified in the treffpunkt protocol document
     * @param affectedAccounts The set of {@code Account} that are affected by
     *                         this {@code Update}
     */
    public void createUpdate(UpdateToSerialize updateToSerialize,
                             Set<? extends Account> affectedAccounts) {
        String updateContent;
        try {
            updateContent = Server.getMapper().writeValueAsString(updateToSerialize);
        } catch (JsonProcessingException e) {
            throw new ProgrammingException(e);
        }
        Update update = new Update();
        update.setContent(updateContent);
        affectedAccounts.forEach(a -> a.addUpdate(update));
        session.save(update);
    }

    /**
     * Convenience method for creating an {@code Update} that affects only a
     * single {@code Account}.
     * <p>
     * Requires the {@code ReadLock} of the affected {@link Account} to be
     * held.
     *
     * @param updateContent   The content of the {@code Update} in the format
     *                        specified in the treffpunkt protocol document
     * @param affectedAccount The {@code Account} that is affected by this
     *                        {@code Update}
     * @see #createUpdate(String, Set)
     */
    public void createUpdate(UpdateToSerialize updateToSerialize, Account affectedAccount) {
        Set<Account> set = new HashSet<>();
        set.add(affectedAccount);
        createUpdate(updateToSerialize, set);
    }
}
