package org.pispeb.treff_client.data.networking;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.pispeb.treff_client.data.entities.Event;
import org.pispeb.treff_client.data.entities.UserGroup;
import org.pispeb.treff_client.data.networking.commands.*;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;

import java.io.IOException;
import java.util.Date;
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
        restartConnection();
        if (idle && !commands.isEmpty()) {
            sendRequest(commands.peek().getRequest());
        }
    }

    private synchronized void executeCommand(AbstractCommand command) {
        Log.i("Encoder", "execute Command");
        commands.add(command);
        //if (idle) {
        sendRequest(commands.peek().getRequest());
        //}
    }

    private synchronized void sendRequest(AbstractRequest request) {
        try {
            if (connectionHandler == null) {
                // is connection is lost, establish new connection
                Log.i("Encoder", "reconnect");
                restartConnection();
            }
            Log.i("Encoder", "sending Message");
            connectionHandler.sendMessage(
                    mapper.writeValueAsString(request));
                idle = false;
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

    private class ConnectTask extends
            AsyncTask<String, String, ConnectionHandler> {
        private ConnectionHandler.OnMessageReceived listener;

        public ConnectTask(ConnectionHandler.OnMessageReceived listener) {
            this.listener = listener;
        }

        @Override
        protected ConnectionHandler doInBackground(String... message) {

            connectionHandler = new ConnectionHandler(
                    "100.85.16.29", 1337, listener);
            connectionHandler.run();

            return null;
        }
    }

    public void closeConnection() {
        connectionHandler.stopClient();
    }

    @Override
    public synchronized void restartConnection() {
        // sets up the connectionHandler and starts it in a different thread
        new ConnectTask(this).execute();
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
     */
    public synchronized void createGroup(String groupName, int[] memberIds) {
        executeCommand(new CreateGroupCommand(groupName, memberIds, TOKEN));
    }

    /**
     * Method to perform an edit-group-name request
     *
     * @param groupId ID of the group
     */
    public synchronized void editGroup(int groupId, String groupName) {
        executeCommand(new EditGroupCommand(groupId, groupName, TOKEN));
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
     * @param timeEnd   The finishing time of the event (unix time
     * @param position  The position of the event
     *
     */
    public synchronized void createEvent(int groupId, String title, int creatorId, Date timeStart,
                                        Date timeEnd, Position position) {
        executeCommand(new CreateEventCommand(groupId, title, creatorId, timeStart,
                timeEnd, position, TOKEN));
    }

    public synchronized void editEvent(int groupId, String title, int creatorId, Date timeStart,
                                       Date timeEnd, Position position, int eventId) {
        executeCommand(new EditEventCommand(groupId, title, creatorId, timeStart,
                timeEnd, position, eventId, TOKEN));
    }

    public synchronized void joinEvent(int groupId, int eventId) {
        executeCommand(new JoinEventCommand(groupId, eventId, TOKEN));
    }

    public synchronized void leaveEvent(int groupId, int eventId) {
        executeCommand(new LeaveEventCommand(groupId, eventId, TOKEN));
    }

    public synchronized void removeEvent(int groupId, int eventId) {
        executeCommand(new RemoveEventCommand(groupId, eventId, TOKEN));
    }

    public synchronized void createPoll(int groupId, String question, boolean isMultiChoice,
                                        Date timeVoteClose) {
        executeCommand(new CreatePollCommand(groupId, question,
                isMultiChoice, timeVoteClose, TOKEN));
    }

    public synchronized void editPoll(int groupId, String question, boolean isMultiChoice,
                                      Date timeVoteClose, int id) {
        executeCommand(new EditPollCommand(groupId, question, isMultiChoice,
                timeVoteClose, id, TOKEN));
    }

    public synchronized void addPollOption(int groupId, int pollId, long latitude, long longitude,
                                           Date timeStart, Date timeEnd) {
        executeCommand(new AddPollOptionCommand(groupId, pollId, latitude,
                longitude, timeStart, timeEnd, TOKEN));
    }

    public synchronized void editPollOption(int groupId, int pollId, long latitude, long longitude,
                                            Date timeStart, Date timeEnd, int optionId) {
        executeCommand(new EditPollOptionCommand(groupId, pollId, latitude, longitude,
                timeStart, timeEnd, optionId, TOKEN));
    }

    public synchronized void voteForOption(int groupId, int pollId, int id) {
        executeCommand(new VoteForOptionCommand(groupId, pollId, id, TOKEN));
    }

    public synchronized void withdrawVoteForOption(int groupId, int pollId, int id) {
        executeCommand(new WithdrawVoteForOptionCommand(groupId, pollId, id, TOKEN));
    }

    public synchronized void removePollOption(int groupId, int pollId, int id) {
        executeCommand(new RemovePollOptionCommand(groupId, pollId, id, TOKEN));
    }

    public synchronized void removePoll(int groupId, int pollId) {
        executeCommand(new RemovePollCommand(groupId, pollId, TOKEN));
    }

    public synchronized void sendChatMessage(int groupId, String message) {
        executeCommand(new SendChatMessageCommand(groupId, message, TOKEN));
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
