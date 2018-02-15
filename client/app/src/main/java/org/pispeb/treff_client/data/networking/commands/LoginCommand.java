package org.pispeb.treff_client.data.networking.commands;

import android.util.Log;

/**
 * Class representing a JSON login object
 */

public class LoginCommand {

    public final String cmd = "login";
    public String user;
    public String pass;

    public LoginCommand (String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
}
