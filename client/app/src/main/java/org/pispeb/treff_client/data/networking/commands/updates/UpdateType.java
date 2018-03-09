package org.pispeb.treff_client.data.networking.commands.updates;

/**
 * The types of an Update.
 */
public enum UpdateType {
    /**
     * The type of updates that represent changes to an account
     */
    ACCOUNT_CHANGE(AccountChangeUpdate.class),
    /**
     * The type of updates that represent a deletion of an account
     */
    ACCOUNT_DELETION(AccountDeletionUpdate.class),
    /**
     * The type of updates that represent changes to an usergroup
     */
    USERGROUP_CHANGE(UsergroupChangeUpdate.class),
    /**
     * The type of updates that represent changes to an event
     */
    EVENT_CHANGE(EventChangeUpdate.class),
    /**
     * The type of updates that represent deletion of an event
     */
    EVENT_DELETION(EventDeletionUpdate.class),
    /**
     * The type of updates that represent changes to a poll
     */
    POLL_CHANGE(PollChangeUpdate.class),
    /**
     * The type of updates that represent deletion of a poll
     */
    POLL_DELETION(PollDeletionUpdate.class),
    /**
     * The type of updates that represent changes to a poll-option
     */
    POLL_OPTION_CHANGE(PollOptionChangeUpdate.class),
    /**
     * The type of updates that represent deletion of a poll-option
     */
    POLL_OPTION_DELETION(PollOptionDeletionUpdate.class),
    /**
     * The type of updates that represent changes to an account
     */
    GROUP_MEMBERSHIP_CHANGE(GroupMembershipChangeUpdate.class),
    /**
     * The type of updates that represent chat messages sent to
     * Usergroups
     */
    CHAT(ChatUpdate.class),
    /**
     * The type of updates that represent a position request
     */
    POSITION_REQUEST(PositionRequestUpdate.class),
    /**
     * The type of updates that represent a contact request
     */
    CONTACT_REQUEST(ContactRequestUpdate.class),
    /**
     * The type of updates that represent a position change
     */
    POSITION_CHANGE(PositionChangeUpdate.class),
    /**
     * The type of updates that represent an answer to a contact request
     */
    CONTACT_REQUEST_ANSWER(ContactRequestUpdate.class),
    /**
     * The type of updates that represent someone canceling a contact
     * request
     */
    CANCEL_CONTACT_REQUEST(CancelContactRequestUpdate.class),
    /**
     * The type of updates that represent someone removing a contact
     */
    REMOVE_CONTACT(RemoveContactUpdate.class);

    public final Class<? extends Update> updateClass;

    UpdateType(Class<? extends Update> updateClass) {
        this.updateClass = updateClass;
    }

    @Deprecated
    @Override
    public String toString() {
        return this.name().toLowerCase().replace('_','-');
    }

    public static UpdateType forTypeString(String typeString) {
        return valueOf(typeString
                .replaceAll("-", "_")
                .toUpperCase());
    }

}
