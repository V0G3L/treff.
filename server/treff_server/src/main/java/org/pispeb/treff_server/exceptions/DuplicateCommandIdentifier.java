package org.pispeb.treff_server.exceptions;

/**
 * Thrown to indicate that a command was to be registered with a string
 * identifier that is already in use.
 */
public class DuplicateCommandIdentifier extends IllegalStateException {

    private final String stringIdentifier;

    public DuplicateCommandIdentifier(String stringIdentifier) {
        this.stringIdentifier = stringIdentifier;
    }

    @Override
    public String getMessage() {
        return String.format("Duplicate command string identifier: %s",
                stringIdentifier);
    }
}
