package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON delete-account object
 */

public class DeleteAccountCommand {

    public final String cmd = "delete-account";
    public String pass;
    public String token;

    public DeleteAccountCommand (String pass, String token) {
        this.pass = pass;
        this.token = token;
    }
}
