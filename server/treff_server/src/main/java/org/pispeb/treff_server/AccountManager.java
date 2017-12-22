package org.pispeb.treff_server;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

/**
 * <p>An interface for an underlying account database. The user of this interface
 * can create accounts and supply a username or an email address to retrieve an
 * existing account, represented by an {@link Account} object.</p>
 * <p>The user can also request a dummy {@link Account} object with no actual
 * capabilities but similar method return times used in the protection against
 * timing attacks.</p>
 */
public interface AccountManager {

    /**
     * Checks whether an account with the given username exists.
     * @param username The username to look up
     * @return <code>true</code> if an account with that username exists,
     * <code>false</code> otherwise.
     */
    Account hasAccountWithUsername(String username);

    /**
     * Checks whether an account with the given email exists.
     * @param email The username to look up
     * @return <code>true</code> if an account with that email exists,
     * <code>false</code> otherwise.
     */
    Account hasAccountWithEmail(String email);

    /**
     * Returns the account that is associated with the given username.
     * @param username The username of the account
     * @return An {@link Account} object representing the account with the
     * supplied username. <code>null</code> if no such account exists.
     */
    Account getAccountByUsername(String username);

    /**
     * Returns the account that is associated with the given email address.
     * @param email The email address of the account
     * @return An {@link Account} object representing the account with the
     * supplied email address. <code>null</code> if no such account exists.
     */
    Account getAccountByEmail(String email);

    /**
     * <p>Returns an {@link Account} object that does not represent an actual
     * account but whose methods will take a similar time to return compared to
     * those of an {@link Account} object representing an actual account.</p>
     * <p>All getter-methods will always return either null or, if returning a
     * primitive, the default value. Since this object is stateless, invoking
     * any setter-methods will have no effect on the object's behaviour.</p>
     * <p>This object may be used as part of a protection against timing
     * attacks.</p>
     * @return An dummy {@link Account} object
     */
    Account getDummyAccount();

    /**
     * Creates a new account with the supplied username, email address and password.
     * @param username The username for the new account
     * @param email The email of the new account
     * @param password The password of the new account
     * @return An {@link Account} object representing the new account.
     * @throws DuplicateUsernameException if the supplied username is already associated with
     * another account. No account will be created in this case.
     * @throws DuplicateEmailException if the supplied email address is already associated with
     * another account. No account will be created in this case.
     */
    Account createAccount(String username, String email, String password)
            throws DuplicateEmailException, DuplicateUsernameException;
}
