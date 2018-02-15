package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON cancel-contact-request object
 */

public class CancelContactRequestCommand {

    public final String cmd = "cancel-contact-request";
    public int id;
    public String token;

    public CancelContactRequestCommand (int id, String token) {
        this.id = id;
        this.token = token;
    }
}
