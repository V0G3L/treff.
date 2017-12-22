package org.pispeb.treff_server;

import org.pispeb.treff_server.exceptions.DuplicateEmailException;
import org.pispeb.treff_server.exceptions.DuplicateUsernameException;

/**
 * An object representing an account with an associated username, email address,
 * and password. An account may only be represented by a single {@link Account}
 * object at a time.
 */
public interface Account {
    /** Returns the username of this account. **/
    String getUsername();

    /** Sets the username of this account.
     * @param username The new username
     * @throws DuplicateUsernameException if another account is already associated with
     * the supplied username. The username of this account is unchanged in
     * this case. */
    void setUsername(String username) throws DuplicateUsernameException;

    /** Checks whether the supplied password matches the password of this account.
     * @param password The password to check
     * @return <code>true</code> if supplied password matches the password of
     * this account, <code>false</code> otherwise. */
    boolean checkPassword(String password);

    /** Sets the password of this account.
     * @param password The new password */
    void setPassword(String password);

    /** Returns the email of this account. **/
    String getEmail();

    /** Sets the email address of this account.
     * @param email The new email address
     * @throws DuplicateEmailException if another account is already associated with the
     * supplied email address. The email address of this account is unchanged in
     * this case. */
    void setEmail(String email) throws DuplicateEmailException;
}
