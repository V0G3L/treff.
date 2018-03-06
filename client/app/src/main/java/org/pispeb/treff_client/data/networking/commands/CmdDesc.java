package org.pispeb.treff_client.data.networking.commands;

/**
 * Mapping Commands to their tags in json objects
 */

public enum CmdDesc {
    ACCEPT_CONTACT_REQUEST ("accept-contact-request"),
    ADD_GROUP_MEMBERS("add-group-members"),
    ADD_POLL_OPTION ("add-poll-option"),
    BLOCK_ACCOUNT ("block-account"),
    CANCEL_CONTACT_REQUEST ("cancel-contact-request"),
    CREATE_EVENT ("create-event"),
    CREATE_GROUP ("create-group"),
    CREATE_POLL ("create-poll"),
    DELETE_ACCOUNT ("delete-account"),
    EDIT_EMAIL ("edit-email"),
    EDIT_EVENT ("edit-event"),
    EDIT_GROUP ("edit-group"),
    EDIT_PASSWORD ("edit-password"),
    EDIT_POLL ("edit-poll"),
    EDIT_POLLOPTION ("edit-polloption"),
    EDIT_USERNAME ("edit-username"),
    GET_CONTACT_LIST ("get-contact-list"),
    GET_EVENT_DETAILS ("get-event-details"),
    GET_GROUP_DETAILS ("get-group-details"),
    GET_PERMISSIONS ("get-permission"),
    GET_POLL_DETAILS ("get-poll-details"),
    GET_USER_DETAILS ("get-user-details"),
    GET_USER_ID ("get-user-id"),
    JOIN_EVENT ("join-event"),
    LEAVE_EVENT ("leave-event"),
    LIST_GROUPS ("list-groups"),
    LOGIN ("login"),
    PUBLISH_POSITION ("publish-position"),
    REGISTER ("register"),
    REJECT_CONTACT_REQUEST ("reject-contact-request"),
    REMOVE_CONTACT ("remove-contact"),
    REMOVE_EVENT ("remove-event"),
    REMOVE_GROUP_MEMBERS ("remove-group-members"),
    REMOVE_POLL ("remove-poll"),
    REMOVE_POLL_OPTION ("remove-poll-option"),
    REQUEST_POSITION ("request-position"),
    REQUEST_UPDATES ("request-updates"),
    RESET_PASSWORD ("reset-password"),
    RESET_PASSWORD_CONFIRM ("reset-password-confirm"),
    SEND_CHAT_MESSAGE ("send-chat-message"),
    SEND_CONTACT_REQUEST ("send-contact-request"),
    UNBLOCK_ACCOUNT ("unblock-account"),
    UPDATE_POSITION ("update-position"),
    VOTE_FOR_OPTION ("vote-for-option"),
    WITHDRAW_VOTE ("withdraw-vote-for-option");

    private final String tag;

    CmdDesc(String tag) {
        this.tag = tag;
    }

    public String toString() {
        return tag;
    }
}
