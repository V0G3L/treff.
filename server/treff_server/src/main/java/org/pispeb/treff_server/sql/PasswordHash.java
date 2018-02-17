package org.pispeb.treff_server.sql;

import org.apache.commons.codec.binary.Hex;

/**
 * @author tim
 */
class PasswordHash {

    final byte[] hash;
    final byte[] salt;

    PasswordHash(byte[] hash, byte[] salt) {
        this.hash = hash;
        this.salt = salt;
    }

    String getHashAsHex() {
        return Hex.encodeHexString(hash);
    }

    String getSaltAsHex() {
        return Hex.encodeHexString(salt);
    }
}
