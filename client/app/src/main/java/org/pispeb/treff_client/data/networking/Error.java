package org.pispeb.treff_client.data.networking;

import android.content.res.Resources;

import org.pispeb.treff_client.R;

/**
 * Errorcode Enum to map from code to message
 */

public enum Error {
    //TODO add translations

    ERRORCODE_INVALID (-1, "Unknown error code", false),

    WRONG_PARAMETERS (1000, "Parameter in wrong format", false),
    UNKNOWN_COMMAND (1001, "Cmd Parameter unknown", false),
    TOKEN_INV (1100, "authentication token invalid", true),
    LOGIN_INV (1101, "Invalid username/password", true),
    PW_RESET_INV (1102, "Password reset code invalid", false),
    USER_ID_INV (1200, "at least one account identification number is invalid",
            false),
    GROUP_ID_INV (1201, "group identification number is invalid or account " +
            "not part of the group", false),
    EVENT_ID_INV (1202, "event identification number invalid", false),
    POLL_ID_INV (1203, "poll identification number invalid", false),
    POLL_OPTION_ID_INV (1204, "poll option identification number invalid",
            false),
    USERNAME_USED (1300, "username already in use", true),
    EMAIL_INV (1301, "email is invalid", true),
    USERNAME_INV (1302, "username is invalid", true),
    END_TIME_IN_PAST (1400, "at least one end time is in the past", false),
    END_BEFORE_START (1401, "at least one end time is not after the " +
            "corresponding start time", false),
    MEASURE_IN_FUTURE (1402, "the time of measurement is too far in the " +
            "future", false),
    ALREADY_CONTACT (1500, "account already in contact list", true),
    NOT_CONTACT (1501, "account isn’t part of the own contact list", false),
    REQUEST_PENDING (1502, "account can’t be a contact of itself", true),
    NO_REQUEST (1504, "no contact request from specified user", false),
    USER_BLOCKED (1505, "this account was blocked by the given user", true),
    BLOCKED_BY_USER (1506, "the given account was blocked by this account",
            true),
    NOT_BLOCKED (1507, "the given account wasn’t blocked by this account",
            false),
    SELF_BLOCK (1508, "account can’t block itself", false),
    NO_USER_INVITED (1509, "no account invited except the own account", false),
    USER_ALREADY_IN_GROUP (1510, "at least one account is already part of the" +
            " group", false),
    USER_NOT_IN_GROUP (1511, "at least one account is not part of the group",
            false),
    ALREADY_IN_EVENT (1512, "account already part of this event", false),
    NOT_IN_EVENT (1513, "account no part of this event", false),
    ALREADY_VOTED (1514, "account already voted for this option", true),
    NO_MULTI_CHOICE (1515, "multi-choice is not available for this poll",
            false),
    NOT_VOTED_FOR (1516, "account didn’t vote for this option", false),
    PERM_EDIT_PERM (2000, "account doesn’t possess the permission to edit " +
            "permissions", true),
    PERM_EDIT_GROUP (2100, "account doesn’t possess the permission to edit " +
            "the group", true),
    PERM_EDIT_MEMBERS (2101, "account doesn’t possess the permission to edit " +
            "the list of group members", true),
    PERM_CREATE_EVENT (2200, "account doesn’t possess the permission to " +
            "create events in this group", true),
    PERM_EDIT_EVENT (2201, "account doesn’t possess the permission to edit " +
            "the event", true),
    PERM_CREATE_POLL (2300, "account doesn’t possess the permission to create" +
            " polls in this group", true),
    PERM_EDIT_POLL (2301, "account doesn’t possess the permission to edit the" +
            " poll", true);


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

    public boolean isUserRelevant() {
        return isUserRelevant;
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
