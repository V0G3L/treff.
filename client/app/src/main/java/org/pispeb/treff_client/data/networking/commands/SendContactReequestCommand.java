package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON send-contact-request object
 */

public class SendContactReequestCommand {

    public final String cmd = "send-contact-request";
    public int id;
    public String token;

    public SendContactReequestCommand (int id, String token) {
        this.id = id;
        this.token = token;
    }
}
