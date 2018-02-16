package org.pispeb.treff_client.data.networking;

import android.location.Location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class provides methods to perform the known server requests.
 */

public class RequestEncoder implements ConnectionHandler.OnMessageReceived {

    private final String TOKEN = "someToken";

    // mapper to convert from Pojos to Strings and vice versa
    private final ObjectMapper mapper;

    private ConnectionHandler connectionHandler;
    private boolean idle;

    // Queue of commands waiting to be sent to Server
    Queue<AbstractCommand> commands;

    public RequestEncoder() {
        commands = new LinkedList<>();
        idle = true;
        mapper = new ObjectMapper();
        this.connectionHandler = new ConnectionHandler("", 1, this);
    }

    private synchronized void executeCommand(AbstractCommand command) {
        commands.add(command);
        if (idle) {
            sendRequest(commands.peek().getRequest());
        }
    }

    private synchronized void sendRequest(AbstractRequest request) {
        try {
            connectionHandler.sendMessage(
                    new ObjectMapper().writeValueAsString(request));
        } catch (JsonProcessingException e) {
            // This would mean, that the internal request encoding is messed
            // up, which would be very bad indeed!
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void messageReceived(String message) {
        try {
            mapper.readValue(message, ErrorResponse.class);
        } catch (IOException e) {
            // TODO handle error response
            e.printStackTrace();
            return;
        }

        AbstractCommand c = commands.poll();
        try {
            c.onResponse(
                    mapper.readValue(message, c.getResponseClass()));
        } catch (IOException e) {
            // This would mean, that the internal request encoding is messed
            // up, which would be very bad indeed!
            e.printStackTrace();
        }

        if (!commands.isEmpty()) {
            sendRequest(commands.peek().getRequest());
        } else {
            idle = true;
        }
    }

    public static class ErrorResponse extends AbstractResponse {
        public final int error;

        public ErrorResponse(int error) {
            this.error = error;
        }
    }

    public Error getErrorByCode(int code) {
        for (Error e : Error.values()) {
            if (e.getCode() == code) return e;
        }
        return Error.ERRORCODE_INVALID;
    }

    /**
     * Method to perform a register request
     *
     * @param username .
     * @param password .
     */
    public synchronized void register(String username, String password) {
        executeCommand(new RegisterCommand(username, password));
    }

    /**
     * Method to perform a login request
     *
     * @param username .
     * @param password .
     */
    public synchronized void login(String username, String password) {
        executeCommand(new LoginCommand(username, password));
    }


    /**
     * Method to perform an edit-username request
     * @param username the new username
     */
    public synchronized void editUsername(String username) {
        executeCommand(new EditUsernameCommand(username, TOKEN));
    }

    /**
     * Method to perform an edit-email request
     * @param email the new email
     */
    public synchronized void editEmail(String email) {
        executeCommand(new EditEmailCommand(email, TOKEN));
    }

    /**
     * Method to perform an edit-password request
     * @param password the current password
     * @param newPassword the new password
     */
    public synchronized void editPassword(String password, String newPassword) {
        executeCommand(new EditPasswordCommand(password, newPassword, TOKEN));
    }

    /**
     * Method to perform a reset-password request
     * @param email the new email
     */
    public synchronized void resetPassword(String email) {
        executeCommand(new ResetPasswordCommand(email));
    }

    /**
     * Method to perform a reset-password-confirm request
     * @param code the validation code of the user
     * @param password the new password
     */

    public synchronized void resetPasswordConfirm(String code, String password) {
        executeCommand(new ResetPasswordConfirmCommand(code, password));
    }

    /**
     * Method to perform a delete-account request
     * @param password the password of the user
     */
    public synchronized void deleteAccount(String password) {
        executeCommand(new DeleteAccountCommand(password, TOKEN));
    }

    /**
     * Method to get the ID belonging to a username
     *
     * @param username .
     * @return The user id of the user
     */
    public synchronized void getUserId(String username) {
        executeCommand(new GetUserIdCommand(username, TOKEN));
    }

    /**
     * Method to perform an send-contact-request request
     *
     * @param userId Contact to be added to the friend list
     */
    public synchronized void sendContactRequest(int userId) {
        executeCommand(new SendContactReequestCommand(userId, TOKEN));
    }


    public synchronized void cancelContactRequest(int userId) {
        executeCommand(new CancelContactRequestCommand(userId, TOKEN));
    }

    public synchronized void acceptContactRequest(int userId) {
        executeCommand(new AcceptContactRequestCommand(userId, TOKEN));
    }

    public synchronized void rejectContactRequest(int userId) {
        executeCommand(new RejectContactRequestCommand(userId, TOKEN));
    }

    public synchronized void removeContact(int userId) {
        executeCommand(new RemoveContactCommand(userId, TOKEN));
    }

    public synchronized void getContactList() {
        executeCommand(new GetContactListCommand(TOKEN));
    }

    public synchronized void blockAccount(int userId) {
        executeCommand(new BlockAccountCommand(userId, TOKEN));
    }

    /**
     * Method to perform an unblock-account request
     *
     * @param userId The user to be unblocked
     */
    public synchronized void unblockAccount(int userId) {
        executeCommand(new UnblockAccountCommand(userId, TOKEN));
    }

    /**
     * Method to perform a create-group request
     *
     * @param groupName Name of the new group
     * @param memberIds Array of the ID's of the members
     * @return The ID of the new group
     */
    public synchronized void createGroup(String groupName, Integer[] memberIds) {}

    /**
     * Method to perform an edit-group-name request
     *
     * @param groupId ID of the group
     */
    public synchronized void editGroup(int groupId) {

    }

    /**
     * Method to perform an add-group-member request
     *
     * @param groupId    ID of the group
     * @param newMembers ID of the new member
     * @return true if the request was successful, false if not
     */
    public synchronized void addGroupMembers(int groupId, int[] newMembers) {
        executeCommand(new AddGroupMembersCommand(groupId, newMembers, TOKEN));
    }

    /**
     * Method to perform a remove-group-member request
     *
     * @param groupId The ID of the group
     * @param members The ID of the member to be removed
     */
    public synchronized void removeGroupMembers(int groupId, int[] members) {
        executeCommand(new RemoveGroupMembersCommand(groupId, members, TOKEN));
    }

    public synchronized void getPermissions(int groupId, int userId) {
        executeCommand(new GetPermissionsCommand(groupId, userId, TOKEN));
    }

    /**
     * Method to perform a create-event request
     *
     * @param groupId   ID of the group of the event
     * @param title     The name of the event
     * @param timeStart The starting time of the event (unix time)
     * @param timeEnd   The finishing time of the event (unix time)
     * @param lat       The latitude of the event (decimal digit)
     * @param lon       The longitude of the event (decimal digit)
     * @return The ID of the new event
     */
    public synchronized int createEvent(int groupId, String title,
                                        int timeStart,
                                        int timeEnd, float lat, float lon) {
        return 0;
    }

    //public boolean editEvent() {}

    /**
     * Method to perform a remove-event request
     *
     * @param groupId The ID of the group of the event
     * @param eventId The ID of the event
     * @return true if the request was successful, false if not
     */
    public synchronized boolean removeEvent(int groupId, int eventId) {
        return false;
    }


    /**
     * Method to perform a send-chat-message request
     *
     * @param groupId ID of the group recieving the message
     * @param message The chat message
     * @return true if the request was successful, false if not
     */
    public synchronized boolean sendChatMesaage(int groupId, String message) {
        return false;
    }

    /**
     * Metod to get information about the users groups
     *
     * @return The IDs of the users groups, hashed to an integer value
     */
    public synchronized Integer[] listGroups() {
        return null;
    }

    /**
     * Method to get all details of a group
     *
     * @param groupId The ID of the group
     * @return All details as a group object
     */
    public synchronized UserGroup getGroupDetails(int groupId) {
        return null;
    }

    /**
     * Method to get all details of an event
     *
     * @param eventId ID of the event
     * @param groupId ID of the group of the event
     * @return All details as an event object
     */
    public synchronized Event getEventDetails(int eventId, int groupId) {
        return null;
    }


    /**
     * Method to update the current position to the server
     *
     * @param location The Position to be updated
     * @return true if the request was successful, false if not
     */
    public synchronized boolean updateLocation(Location location) {
        return false;
    }
}
