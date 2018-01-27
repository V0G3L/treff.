package org.pispeb.treff_server.interfaces;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

import javax.json.JsonObject;
import java.util.Date;
import java.util.Map;

/**
 * <p>An interface for an underlying database. The user of this interface
 * can create accounts and supply a username or an email address to retrieve an
 * existing account, represented by an {@link Account} object.</p>
 * <p>The user can also request a dummy {@link Account} object with no actual
 * capabilities but similar method return times used in the protection against
 * timing attacks.</p>
 */
public interface AccountManager {

    // no has/contains method because simple getters are atomic

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

    Map<Integer, Account> getAllAccounts();

    /**
     * Creates a new account with the supplied username and password.
     *
     * @param username The username for the new account
     * @param password The password of the new account
     * @return The created {@link Account}
     * @throws DuplicateUsernameException if the supplied username is already
     * associated with
     *                                    another account. No account will be
     *                                    created in this case.
     * @throws DuplicateEmailException    if the supplied email address is
     * already associated with
     *                                    another account. No account will be
     *                                    created in this case.
     */
    Account createAccount(String username, String password)
            throws DuplicateEmailException, DuplicateUsernameException;

    Account getAccountByLoginToken(String token);

    String generateNewLoginToken(Account account);

    void invalidateLoginToken(Account account);

    void createUpdate(JsonObject updateContent, Date time,
                      Update.UpdateType type, Account... affectedAccounts);

}
