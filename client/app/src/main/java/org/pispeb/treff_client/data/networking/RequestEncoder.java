package org.pispeb.treff_client.data.networking;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.Poll;
import org.pispeb.treff_client.data.entities.PollOption;
import org.pispeb.treff_client.data.entities.Position;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.entities.Event;

/**
 * This class provides methods to perform the known server requests.
 */

public class RequestEncoder {
    private static RequestEncoder INSTANCE;
    private ConnectionHandler connectionHandler;

    private final char SEP1 = (char) 1;
    private final char SEP2 = (char) 2;
    private final char SEP3 = (char) 3;

    public static RequestEncoder getEntity() {
        if(INSTANCE == null) {
            INSTANCE = new RequestEncoder();
        }
        return INSTANCE;
    }

    private RequestEncoder() {
        this.connectionHandler = new ConnectionHandler("", 1);
    }

    public Error getErrorByCode(int code) {
        for (Error e : Error.values()) {
            if(e.getCode() == code) return e;
        }
        return Error.ERRORCODE_INVALID;
    }

    /**
     * Method to perform a login request
     * @param username .
     * @param password .
     * @return A verification token
     */
    public int login(String username, String password) {
        connectionHandler.sendRequest("cmd" + SEP1 + username + SEP2 + password);
        return 0;
    }

    /**
     * Method to get the ID belonging to a username
     * @param username .
     * @return The user id of the user
     */
    public int getUserId(String username) {
        return 0;
    }

    /**
     * Method to perform an add-contact request
     * @param userId Contact to be added to the friend list
     * @return true if the request was succesful, fale if not
     */
    public boolean addContact(int userId) {
        return false;
    }

    /**
     * Method to perform a remove-contact request
     * @param userID Contact to be removed from friend list
     * @return true if the request was succesful, fale if not
     */
    public boolean removeContact(int userID) {
        return false;
    }

    /**
     * Method to perform a list-contacts request
     * @return An array of the ID's of the the friends of the user
     */
    public Integer[] listContacts() { return null;}

    /**
     * Method to perform a block-account request
     * @param userId The user to be blocked
     * @return true if the request was succesful, fale if not
     */
    public boolean blockAccount(int userId) {
        return false;
    }

    /**
     * Method to perform an unblock-account request
     * @param userId The user to be unblocked
     * @return true if the request was succesful, fale if not
     */
    public boolean unblockAccount(int userId) {
        return false;
    }

    /**
     * Method to perform a create-group request
     * @param groupName Name of the new group
     * @param memberIds Array of the ID's of the members
     * @return The ID of the new group
     */
    public int createGroup(String groupName, Integer[] memberIds) { return 0; }

    /**
     * Method to perform an edit-group-name request
     * @param groupId ID of the group
     * @param name New name of the group
     * @return true if the request was succesful, fale if not
     */
    public boolean editGroupName(int groupId, String name) { return false; }

    /**
     * Method to perform an add-group-member request
     * @param groupId ID of the group
     * @param newMembers ID of the new member
     * @return true if the request was succesful, fale if not
     */
    public boolean addGroupMembers(int groupId, Integer[] newMembers) { return false; }

    /**
     * Method to perform a remove-group-member request
     * @param groupId The ID of the group
     * @param members The ID of the member to be removed
     * @return true if the request was succesful, fale if not
     */
    public boolean removeGroupMembers(int groupId, Integer[] members) { return false; }

    /**
     * Method to perform a create-event request
     * @param groupId ID of the group of the event
     * @param title The name of the event
     * @param timeStart The starting time of the event (unix time)
     * @param timeEnd The finishing time of the event (unix time)
     * @param lat The latitude of the event (decimal digit)
     * @param lon The longitude of the event (decimal digit)
     * @return The ID of the new event
     */
    public int createEvent(int groupId, String title, int timeStart,
                           int timeEnd, float lat, float lon) {
        return 0;
    }

    //public boolean editEvent() {}

    /**
     * Method to perform a remove-event request
     * @param groupId The ID of the group of the evebt
     * @param eventId The ID of the event
     * @return true if the request was succesful, fale if not
     */
    public boolean removeEvent(int groupId, int eventId) { return false; }

    /**
     * Method to perform a create-poll request
     * @param groupId ID of the group
     * @param question The question of the poll
     * @param multichoice Defines if multiple answers are allowed
     * @param options The Options of the poll
     * @return The first value of the array is the ID of the poll. The following values are the IDs
     * of the olloptions
     */
    public Integer[] createPoll(int groupId, String question,
                                boolean multichoice, PollOption[] options) {
        return null;
    }

    //public boolean editPoll() {}

    /**
     * Method to perform an add-poll-option request
     * @param groupId ID of the group
     * @param pollId ID of the poll
     * @param lat Latitude of the event resulting in this option (decimal digit)
     * @param lon Longitude of the event resulting in this option (decimal digit)
     * @param start Starting time of the event resulting in this option (unix time)
     * @param end End time of the event resulting in this option (unix time)
     * @return The ID of the new polloption
     */
    public int addPollOption(int groupId, int pollId, float lat, float lon, int start, int end) {
        return 0;
    }

    //public boolean editPollOption(int groupId, int pollId, int optionId, )

    /**
     * Method to perform a remove-poll-option request
     * @param groupId ID of the group
     * @param pollId ID of the poll
     * @param optionId ID of the option to be removed
     * @return true if the request was succesful, fale if not
     */
    public boolean removePollOption(int groupId, int pollId, int optionId) { return false; }

    /**
     * Method to perform a remove-poll request
     * @param groupId ID of the group
     * @param pollId ID of the poll to be removed
     * @return true if the request was succesful, fale if not
     */
    public boolean removePoll(int groupId, int pollId) { return false; }

    /**
     * Method to perform a send-chat-message request
     * @param groupId ID of the group recieving the message
     * @param message The chat message
     * @return true if the request was succesful, fale if not
     */
    public boolean sendChatMesaage(int groupId, String message) { return false; }

    /**
     * Metod to get information about the users groups
     * @return The IDs of the users groups, hashed to an integer value
     */
    public Integer[] listGroups() { return null; }

    /**
     * Method to get all details of a group
     * @param groupId The ID of the group
     * @return All details as a group object
     */
    public UserGroup getGroupDetails(int groupId) { return null; }

    /**
     * Method to get all details of an event
     * @param eventId ID of the event
     * @param groupId ID of the group of the event
     * @return All details as an event object
     */
    public Event getEventDetails(int eventId, int groupId) { return null; }

    /**
     * Method to get all details of a poll
     * @param pollId The ID of the poll
     * @param groupId The ID of the group hosting the poll
     * @return All details as a poll object
     */
    public Poll getPollDetails(int pollId, int groupId) { return null;}

    /**
     * Method to update the current position to the server
     * @param position The Position to be updated
     * @return true if the request was succesful, fale if not
     */
    public boolean updatePosition(Position position) { return false; }
}
