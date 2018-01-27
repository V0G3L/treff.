package org.pispeb.treff_server.sql;

/**
 * @author tim
 */
public class PasswordHash {

    final byte[] hash;
    final byte[] salt;

    public PasswordHash(byte[] hash, byte[] salt) {
        this.hash = hash;
        this.salt = salt;
    }
}
