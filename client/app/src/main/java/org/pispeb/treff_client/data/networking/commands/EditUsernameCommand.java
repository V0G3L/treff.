package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON edit-username object
 */

public class EditUsernameCommand {

    public final String cmd = "edit-username";
    public String username;
    public String token;

    public EditUsernameCommand (String username, String token) {
        this.username = username;
        this.token = token;
    }
}
