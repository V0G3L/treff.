package org.pispeb.treff_client.data.networking;

import android.content.res.Resources;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.view.util.TreffPunkt;

/**
 * Errorcode Enum to map from code to message
 */

public enum Error {
    //TODO add translations

    ERRORCODE_INVALID
            (-1, getString(R.string.error_errorcode_invalid), false),
    WRONG_PARAMETERS
            (1000, getString(R.string.error_wrong_parameters), false),
    UNKNOWN_COMMAND
            (1001, getString(R.string.error_unknown_command), false),
    TOKEN_INV
            (1100, getString(R.string.error_token_inv), true),
    LOGIN_INV
            (1101, getString(R.string.error_login_inv), true),
    PW_RESET_INV
            (1102, getString(R.string.error_pw_reset_inv), false),
    USER_ID_INV
            (1200, getString(R.string.error_user_id_inv), false),
    GROUP_ID_INV
            (1201, getString(R.string.error_group_id_inv), false),
    EVENT_ID_INV
            (1202, getString(R.string.error_event_id_inv), false),
    POLL_ID_INV
            (1203, getString(R.string.error_poll_id_inv), false),
    POLL_OPTION_ID_INV
            (1204, getString(R.string.error_poll_option_id_inv), false),
    USERNAME_USED
            (1300, getString(R.string.error_username_used), true),
    EMAIL_INV
            (1301, getString(R.string.error_email_inv), true),
    USERNAME_INV
            (1302, getString(R.string.error_username_inv), true),
    END_TIME_IN_PAST
            (1400, getString(R.string.error_end_time_in_past), false),
    END_BEFORE_START
            (1401, getString(R.string.error_end_before_start), false),
    MEASURE_IN_FUTURE
            (1402, getString(R.string.error_measure_in_future), false),
    ALREADY_CONTACT
            (1500, getString(R.string.error_already_contact), true),
    NOT_CONTACT
            (1501, getString(R.string.error_not_contact), false),
    REQUEST_PENDING
            (1502, getString(R.string.error_request_pending), true),
    NO_REQUEST
            (1504, getString(R.string.error_no_request), false),
    USER_BLOCKED
            (1505, getString(R.string.error_user_blocked), true),
    BLOCKED_BY_USER
            (1506, getString(R.string.error_blocked_by_user), true),
    NOT_BLOCKED
            (1507, getString(R.string.error_not_blocked), false),
    SELF_BLOCK
            (1508, getString(R.string.error_self_block), false),
    NO_USER_INVITED
            (1509, getString(R.string.error_no_user_invited), false),
    USER_ALREADY_IN_GROUP
            (1510, getString(R.string.error_user_already_in_group), false),
    USER_NOT_IN_GROUP
            (1511, getString(R.string.error_user_not_in_group), false),
    ALREADY_IN_EVENT
            (1512, getString(R.string.error_already_in_event), false),
    NOT_IN_EVENT
            (1513, getString(R.string.error_not_in_event), false),
    ALREADY_VOTED
            (1514, getString(R.string.error_already_voted), true),
    NO_MULTI_CHOICE
            (1515, getString(R.string.error_no_multi_choice), false),
    NOT_VOTED_FOR
            (1516, getString(R.string.error_not_voted_for), false),
    PERM_EDIT_PERM
            (2000, getString(R.string.error_perm_edit_perm), true),
    PERM_EDIT_GROUP
            (2100, getString(R.string.error_perm_edit_group), true),
    PERM_EDIT_MEMBERS
            (2101, getString(R.string.error_perm_edit_members), true),
    PERM_CREATE_EVENT
            (2200, getString(R.string.error_perm_create_event), true),
    PERM_EDIT_EVENT
            (2201, getString(R.string.error_perm_edit_event), true),
    PERM_CREATE_POLL
            (2300, getString(R.string.error_perm_create_poll), true),
    PERM_EDIT_POLL
            (2301, getString(R.string.error_perm_edit_poll), true);


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

    private static String getString(int resourceId) {
        return TreffPunkt.getAppContext().getResources().getString(resourceId);
    }
}
