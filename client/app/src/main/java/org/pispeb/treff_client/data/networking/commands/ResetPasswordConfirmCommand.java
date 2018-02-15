package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a JSON reset-password-confirm object
 */

public class ResetPasswordConfirmCommand {

    public final String cmd = "reset-password-confirm";
    public String code;

    @JsonProperty("new-pass")
    public String newPass;

    public ResetPasswordConfirmCommand (String code, String newPass) {
        this.code = code;
        this.newPass = newPass;
    }
}
