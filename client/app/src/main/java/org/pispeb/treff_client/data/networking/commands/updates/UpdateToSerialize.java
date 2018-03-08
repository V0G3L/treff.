package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.CancelContactRequestCommand;
import org.pispeb.treff_client.data.repositories.ChatRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateToSerialize {

    public final String type;
    @JsonProperty("time-created")
    public final Date date;
    @JsonProperty("creator")
    public final int creator;

    private static Map<String, Class<? extends UpdateToSerialize>>
            availableUpdates = new HashMap<>();

    public UpdateToSerialize(String type, Date date, int creator) {
        this.type = type;
        this.date = date;
        this.creator = creator;

        //Adds the possible updates to the map

        availableUpdates.put("account-change", AccountChangeUpdate.class);
        availableUpdates.put("user-group-change", UsergroupChangeUpdate.class);
        availableUpdates.put("event-change", EventChangeUpdate.class);
        availableUpdates.put("poll-change", PollChangeUpdate.class);
        //availableUpdates.put("poll-option-change", PollOptionChange.class);
        availableUpdates.put("group-membership-change", GroupMembershipChangeUpdate.class);
        availableUpdates.put("chat", ChatUpdate.class);
        availableUpdates.put("position-request", PositionRequestUpdate.class);
        availableUpdates.put("contact-request", UpdatesWithoutSpecialParameters.class);
        availableUpdates.put("position", UpdatesWithoutSpecialParameters.class);
        availableUpdates.put("contact-request-answer", ContactRequestAnswerUpdate.class);
        availableUpdates.put("cancel-contact-request", UpdatesWithoutSpecialParameters.class);
        availableUpdates.put("remove-contact", UpdatesWithoutSpecialParameters.class);
        availableUpdates.put("event-deletion", EventDeletionUpdate.class);
        availableUpdates.put("poll-deletion", PollDeletionUpdate.class);
        availableUpdates.put("poll-option-deletion", PollOptionDeletionUpdate.class);

    }

    /**
     *
     * @param stringIdentifier the string representation of the different update types
     * @return the class that belongs to the given identifier
     */
    public static Class<? extends UpdateToSerialize> getUpdateByStringIdentifier(
            String stringIdentifier) {
        return availableUpdates.get(stringIdentifier);
    }
}
