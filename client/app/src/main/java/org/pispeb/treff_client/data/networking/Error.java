package org.pispeb.treff_client.data.networking;

/**
 * Errorcode Enum to map from code to message
 */

public enum Error {
    //TODO add errors

    ERRORCODE_INVALID (-10, "Unbekannter Fehlercode", false),
    USERNAME_TAKEN (0, "Nutzername ist bereits vergeben", true),
    EMAIL_INVALID (10, "Die eingegebene Email ist nicht gültig", true),
    TOKEN_INVALID (20, "Die Session ist abgelaufen, bitte erneut anmelden", true);

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
}
