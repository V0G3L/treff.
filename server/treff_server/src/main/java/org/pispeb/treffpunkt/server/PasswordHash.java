package org.pispeb.treffpunkt.server;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author tim
 */
public class PasswordHash {

    public final byte[] hash;

    public final byte[] salt;

    // TODO: set based on config
    private static String hashAlg = "SHA-512";
    private static int saltBytes = 32;

    public PasswordHash(byte[] hash, byte[] salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public static PasswordHash generatePasswordHash(String password) {
        // Generate a new salt with a cryptographically strong RNG
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[saltBytes];
        secureRandom.nextBytes(salt);
        return generatePasswordHash(password, salt);
    }

    public static PasswordHash generatePasswordHash(String password,
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
}
