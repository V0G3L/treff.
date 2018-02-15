package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON accept-contact-request object
 */

public class AcceptContactRequestCommand {

    public final String cmd = "accept-contact-request";
    public int id;
    public String token;

    public AcceptContactRequestCommand (int id, String token) {
        this.id = id;
        this.token = token;
    }
}
