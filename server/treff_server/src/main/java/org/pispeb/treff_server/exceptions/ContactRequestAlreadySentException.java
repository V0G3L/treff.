package org.pispeb.treff_server.exceptions;

/**
 * Thrown to indicate that a contact request was to be sent while a there is
 * still a pending contact request with the same sender and receiver.
 */
public class ContactRequestAlreadySentException
        extends IllegalArgumentException {
}
