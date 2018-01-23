package org.pispeb.treff_client.data.networking;

import android.content.res.Resources;

import org.pispeb.treff_client.R;

/**
 * Errorcode Enum to map from code to message
 */

public enum Error {
    //TODO add errors


    ERRORCODE_INVALID (-10, sys().getString(R.string.error_errorcode_invalid), false),
    USERNAME_TAKEN (0, sys().getString(R.string.error_username_taken), true),
    EMAIL_INVALID (10, sys().getString(R.string.error_email_invalid), true),
    TOKEN_INVALID (20, sys().getString(R.string.error_session), true);

    private final int code;
    private final String message;
    private final boolean isUserRelevant;

    Error(int code, String message, boolean isUserRelevant) {
        this.code = code;
        this.message = message;
        this.isUserRelevant = isUserRelevant;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static Error getValue(int code) {
        for (Error e : Error.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return ERRORCODE_INVALID;
    }

    private static Resources sys() {
        return Resources.getSystem();
    }
}
