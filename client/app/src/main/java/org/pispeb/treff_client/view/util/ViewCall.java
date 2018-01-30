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
    DISPLAY_EOP_DIALOG,
    DISPLAY_SETTINGS,
    LOGIN,
    REGISTER,
    GO_TO_LOGIN,
    GO_TO_REGISTER,
    SUCCESS,
    CENTER_MAP,
    UPDATE_VIEW
}
