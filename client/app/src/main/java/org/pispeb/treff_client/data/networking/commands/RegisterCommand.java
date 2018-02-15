package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON register object
 */

public class RegisterCommand {

    public final String cmd = "register";
    public String user;
    public String pass;

    public RegisterCommand (String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
}
