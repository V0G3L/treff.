package org.pispeb.treff_client.view.util;

/**
 * All possible calls that ViewModels can set their state to, to notify the
 * Context they belong to in order for them to take certain actions
 */

public enum ViewCall {
    IDLE,
    DISPLAY_FRIEND_DETAILS,
    DISPLAY_GROUP_DETAILS,
    ADD_GROUP,
    ADD_FRIEND,
    ADD_EVENT,
    DISPLAY_SETTINGS,
    LOGIN,
    REGISTER,
    GO_TO_LOGIN,
    GO_TO_REGISTER,
    SUCCESS,
    CENTER_MAP,
    UPDATE_VIEW,
    SHOW_FILTER_DIALOG,
    SHOW_DATE_DIALOG,
    SHOW_TIME_DIALOG,
    LEFT_GROUP,
    SHOW_ADD_MEMBER_DIALOG,
    SHOW_PENDING_DIALOG,
    SHOW_REQUESTING_DIALOG,
    SHOW_BLOCKED_DIALOG,
    EDIT_DATA,
    EDIT_PASSWORD

}
