package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON edit-email object
 */

public class EditEmailCommand {

    public final String cmd = "edit-email";
    public String email;
    public String token;

    public EditEmailCommand (String email, String token) {
        this.email = email;
        this.token = token;
    }
}