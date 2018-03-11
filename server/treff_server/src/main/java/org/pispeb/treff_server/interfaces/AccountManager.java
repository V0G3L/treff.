package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

import java.util.Map;
import java.util.Set;

/**
 * An interface for an underlying database that allows retrieval of
 * creation of {@link Account}s and creation of {@link Update}s.
 * The user of this interface can supply a username or an email address to
 * retrieve an existing account, represented by an {@link Account} object.
 */
public interface AccountManager {

    // no has/contains methods because simple getters are atomic
    // this prevents having to lock the AccountManager

    /**
     * Returns the account that is associated with the given username.
     *
     * @param username The username of the account
     * @return An {@link Account} object representing the account with the
     * supplied username or <code>null</code> if no such account exists.
     */
    Account getAccountByUsername(String username);

    /**
     * Returns the account that is associated with the given email address.
     *
     * @param email The email address of the account
     * @return An {@link Account} object representing the account with the
     * supplied email address or <code>null</code> if no such account exists.
     */
    Account getAccountByEmail(String email);

    /**
     * Returns the account that is associated with the given ID.
     *
     * @param id The ID of the account
     * @return An {@link Account} object representing the account with the
     * supplied ID or <code>null</code> if no such account exists.
     */
    Account getAccount(int id);

    /**
     * Returns an unmodifiable [ID -> {@code Account}] map holding all
     * {@code Account}s that have been registered and not deleted.
     *
     * @return Unmodifiable [ID -> {@code Account}] map
     * @see java.util.Collections#unmodifiableMap(Map)
     */
    Map<Integer, ? extends Account> getAllAccounts();

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
    Account createAccount(String username, String password)
            throws DuplicateUsernameException;

    /**
     * Returns the account that is associated with the given login token.
     *
     * @param token The login token of the account
     * @return An {@link Account} object representing the account with the
     * supplied login token or <code>null</code> if no such account exists.
     */
    Account getAccountByLoginToken(String token);

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
     *  @param updateContent The content of the {@code Update} in the format
     *                      specified in the treffpunkt protocol document
     * @param affectedAccounts The set of {@code Account} that are affected by
     *                         this {@code Update}
     */
    void createUpdate(String updateContent,
                      Set<? extends Account> affectedAccounts);

    /**
     * Convenience method for creating an {@code Update} that affects only a
     * single {@code Account}.
     * <p>
     * Requires the {@code ReadLock} of the affected {@link Account} to be
     * held.
     *
     * @param updateContent The content of the {@code Update} in the format
     *                      specified in the treffpunkt protocol document
     * @param affectedAccount The {@code Account} that is affected by this
     *                        {@code Update}
     * @see #createUpdate(String, Set)
     */
    void createUpdate(String updateContent,
                      Account affectedAccount);

}
