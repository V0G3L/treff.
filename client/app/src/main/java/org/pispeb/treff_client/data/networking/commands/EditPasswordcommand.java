package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a JSON edit-password object
 */

public class EditPasswordcommand {

    public final String cmd = "edit-password";
    public String pass;

    @JsonProperty("new-pass")
    public String newPass;

    public String token;

    public EditPasswordcommand (String pass, String newPass, String token) {
        this.pass = pass;
        this.newPass = newPass;
        this.token = token;
    }
}
