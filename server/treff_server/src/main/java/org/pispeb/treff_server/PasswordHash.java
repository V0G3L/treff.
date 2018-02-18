package org.pispeb.treff_server;

import org.apache.commons.codec.binary.Hex;

/**
 * @author tim
 */
public class PasswordHash {

    public final byte[] hash;
    public final byte[] salt;

    public PasswordHash(byte[] hash, byte[] salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHashAsHex() {
        return Hex.encodeHexString(hash);
    }

    public String getSaltAsHex() {
        return Hex.encodeHexString(salt);
    }
}
