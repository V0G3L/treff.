package org.pispeb.treff_server.commands;

import java.util.regex.Pattern;

/**
 * @author tim
 */
abstract class CommandInput {

    // compile pattern only once
    private static final Pattern alphanumericPattern
            = Pattern.compile("[^a-zA-Z0-9]");

    abstract boolean syntaxCheck();

    static boolean stringMaxLength(String string, int maxLength) {
        return string.length() <= maxLength;
    }

    static boolean stringAlphanumeric(String string) {
        // check if string contains any non-alphanumeric char, then invert
        return !alphanumericPattern.matcher(string).find();
    }
}
