package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a JSON reset-password object
 */

public class ResetPasswordCommand {

    public final String cmd = "reset-password";
    public String email;

    public ResetPasswordCommand (String email) {
        this.email = email;
    }
}
