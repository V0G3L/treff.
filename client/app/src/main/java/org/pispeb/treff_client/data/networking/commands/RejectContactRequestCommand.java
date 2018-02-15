package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON reject-contact-request object
 */

public class RejectContactRequestCommand {

    public final String cmd = "reject-contact-request";
    public int id;
    public String token;

    public RejectContactRequestCommand (int id, String token) {
        this.id = id;
        this.token = token;
    }
}
