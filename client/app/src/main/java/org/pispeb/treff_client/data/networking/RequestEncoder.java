package org.pispeb.treff_client.data.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.database.TreffDatabase;
import org.pispeb.treff_client.data.networking.commands.*;
import org.pispeb.treff_client.data.networking.commands.descriptions.Position;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;
import org.pispeb.treff_client.view.util.TreffPunkt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import javax.websocket.DeploymentException;

/**
 * This class provides methods to perform the known server requests.
 */

public class RequestEncoder implements ConnectionHandler.ResponseListener {

    private final int DISPLAY_ERROR_TOAST = 1;

    // mapper to convert from Pojos to Strings and vice versa
    private final ObjectMapper mapper;

    private ConnectionHandler connectionHandler;
    private boolean idle;

    private Handler bgHandler;
    private Handler uiHandler;


    private UserRepository userRepository;
    private UserGroupRepository userGroupRepository;
    private EventRepository eventRepository;
    private ChatRepository chatRepository;

    // Queue of commands waiting to be sent to Server
    Queue<AbstractCommand> commands;
    private CountDownLatch cdl;

    private static RequestEncoder INSTANCE;

    public static RequestEncoder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestEncoder();
        }

        return INSTANCE;
    }

    private RequestEncoder() {
        commands = new LinkedList<>();
        idle = true;

        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature
                .FAIL_ON_MISSING_CREATOR_PROPERTIES);

        try {
            connectionHandler
                    = new ConnectionHandler(
                            "ws://192.168.0.136:8080/adasdasd/ws",
                    this);
        } catch (URISyntaxException | IOException | DeploymentException e) {
            e.printStackTrace(); // TODO: TODONT
        }
        HandlerThread thread = new HandlerThread("gbt",
                HandlerThread.MIN_PRIORITY);
        thread.start();
        bgHandler = new Handler(thread.getLooper());
        cdl = new CountDownLatch(0);

        uiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message.what == DISPLAY_ERROR_TOAST) {
                    Error error = (Error) message.obj;
                    TreffPunkt.showToast(error.getMessage());
                }
            }
        };

        if (idle && !commands.isEmpty()) {
            sendRequest(commands.peek().getRequest());
        }
    }

    // must be called right after creating the encoder
    public void setRepos(
            UserRepository userRepository,
            UserGroupRepository userGroupRepository,
            EventRepository eventRepository,
            ChatRepository chatRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.eventRepository = eventRepository;
        this.chatRepository = chatRepository;
    }

    /**
     * TODO: doc
     *
     * @param command
     */
    private synchronized void executeCommand(AbstractCommand command) {
        Log.i("Encoder", "execute Command" + commands.size());
        commands.add(command);
        if (idle) {
            sendRequest(commands.peek().getRequest());
        }
    }

    /**
     * TODO: doc
     *
     * @param request
     */
    private synchronized void sendRequest(AbstractRequest request) {
        bgHandler.post(() -> {
            try {
                Log.i("Encoder", "sending Message");
                Log.i("Encoder", "CH: " + connectionHandler);
                connectionHandler.sendMessage(
                        mapper.writeValueAsString(request));
                idle = false;
            } catch (JsonProcessingException e) {
                // This would mean, that the internal request encoding is messed
                // up, which would be very bad indeed!
                e.printStackTrace();
            }
        });
    }

    private String getToken() {
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        return pref.getString(ctx.getString(R.string.key_token), "");
    }

    private int getUserId() {
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        return pref.getInt(ctx.getString(R.string.key_userId), -1);
    }

    @Override
    public void onResponse(String responseString) {
        // no commands waiting for response
        if (commands.isEmpty()) {
            //should never happen as long as server is working correctly
            return;
        }
        Log.i("RequestEncoder", "Message received: " + responseString);
        AbstractCommand c = commands.poll();
        try {
            ErrorResponse error = mapper
                    .readValue(responseString, ErrorResponse.class);
            Log.i("RequestEncoder", "Error");
            handleError(error);
        } catch (IOException e) {
            // it is no Error
            try {
                AbstractResponse response = mapper.readValue(responseString,
                        c.getResponseClass());
                c.onResponse(response);
            } catch (IOException ex) {
                // This would mean, that the internal request encoding is messed
                // up, which would be very bad indeed!
                ex.printStackTrace();
            }
        }

        if (!commands.isEmpty()) {
            sendRequest(commands.peek().getRequest());
        } else {
            idle = true;
        }
    }

    /**
     * TODO: doc
     */
    public static class ErrorResponse extends AbstractResponse {
        public final int error;

        public ErrorResponse(@JsonProperty("error") int error) {
            this.error = error;
        }
    }

    private void handleError(ErrorResponse response) {
        //TODO handle Errors
        Error error = Error.getValue(response.error);
        Log.e("RequestEncoder", error.getCode() +
                ": " + error.getMessage());
        if (error.isUserRelevant()) {
            Message message = uiHandler.obtainMessage(DISPLAY_ERROR_TOAST,
                    error);
            message.sendToTarget();
        }

        if (error == Error.TOKEN_INV) {
            // TODO handle user logged out
        }
    }

    /**
     * TODO: doc
     */
    public void closeConnection() {
        // TODO check for Service still running

        connectionHandler.disconnect();
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
     *
     * @param username the new username
     */
    public synchronized void editUsername(String username) {
        executeCommand(new EditUsernameCommand(username, getToken()));
    }

    /**
     * Method to perform an edit-email request
     *
     * @param email the new email
     */
    public synchronized void editEmail(String email) {
        executeCommand(new EditEmailCommand(email, getToken()));
    }

    /**
     * Method to perform an edit-password request
     *
     * @param password    the current password
     * @param newPassword the new password
     */
    public synchronized void editPassword(String password, String newPassword) {
        executeCommand(new EditPasswordCommand(password, newPassword, getToken()));
    }

    /**
     * Method to perform a reset-password request
     *
     * @param email the new email
     */
    public synchronized void resetPassword(String email) {
        executeCommand(new ResetPasswordCommand(email));
    }

    /**
     * Method to perform a reset-password-confirm request
     *
     * @param code     the validation code of the user
     * @param password the new password
     */

    public synchronized void resetPasswordConfirm(String code,
                                                  String password) {
        executeCommand(new ResetPasswordConfirmCommand(code, password));
    }

    /**
     * Method to perform a delete-account request
     *
     * @param password the password of the user
     */
    public synchronized void deleteAccount(String password) {
        executeCommand(new DeleteAccountCommand(password, getToken()));
    }

    /**
     * Method to get the ID belonging to a username
     *
     * @param username .
     * @return The user id of the user
     */
    public synchronized void getUserId(String username) {
        executeCommand(new GetUserIdCommand(username, getToken(), userRepository));
    }

    /**
     * Method to perform an send-contact-request request
     *
     * @param userId Contact to be added to the friend list
     */
    public synchronized void sendContactRequest(int userId) {
        executeCommand(new SendContactRequestCommand(userId, getToken()));
    }

    /**
     * Method to perform an send-contact-request request
     *
     * @param userName Contact to be added to the friend list
     */
    public synchronized void sendContactRequest(String userName) {
        executeCommand(new GetUserIdCommand(userName, getToken(), userRepository));
        executeCommand(new SendContactRequestCommand(userName, getToken(),
                userRepository));
    }


    public synchronized void cancelContactRequest(int userId) {
        executeCommand(new CancelContactRequestCommand(userId, getToken(),
                userRepository));
    }

    public synchronized void acceptContactRequest(int userId) {
        executeCommand(new AcceptContactRequestCommand(userId, getToken(),
                userRepository));
    }

    public synchronized void rejectContactRequest(int userId) {
        executeCommand(new RejectContactRequestCommand(userId, getToken(),
                userRepository));
    }

    public synchronized void removeContact(int userId) {
        executeCommand(new RemoveContactCommand(userId, getToken(), userRepository));
    }

    public synchronized void getContactList() {
        executeCommand(new GetContactListCommand(getToken(), userRepository));
    }

    public synchronized void blockAccount(int userId) {
        executeCommand(new BlockAccountCommand(userId, getToken(), userRepository));
    }

    /**
     * Method to perform an unblock-account request
     *
     * @param userId The user to be unblocked
     */
    public synchronized void unblockAccount(int userId) {
        executeCommand(
                new UnblockAccountCommand(userId, getToken(), userRepository));
    }

    /**
     * Method to perform a create-group request
     *
     * @param groupName Name of the new group
     * @param memberIds Array of the ID's of the members
     */
    public synchronized void createGroup(String groupName, int[] memberIds) {
        executeCommand(new CreateGroupCommand(groupName, memberIds, getToken(),
                userGroupRepository));
    }

    /**
     * Method to perform an edit-group-name request
     *
     * @param groupId ID of the group
     */
    public synchronized void editGroup(int groupId, String groupName) {
        executeCommand(new EditGroupCommand(groupId, groupName, getToken(),
                userGroupRepository));
    }

    /**
     * Method to perform an requestAddUser-group-member request
     *
     * @param groupId    ID of the group
     * @param newMembers ID of the new member
     * @return true if the request was successful, false if not
     */
    public synchronized void addGroupMembers(int groupId, int[] newMembers) {
        executeCommand(new AddGroupMembersCommand(groupId, newMembers, getToken(),
                userGroupRepository));
    }

    /**
     * Method to perform a remove-group-member request
     *
     * @param groupId The ID of the group
     * @param members The ID of the member to be removed
     */
    public synchronized void removeGroupMembers(int groupId, int[] members) {
        executeCommand(new RemoveGroupMembersCommand(groupId, members, getToken(),
                userGroupRepository));
    }

    public synchronized void getPermissions(int groupId, int userId) {
        executeCommand(new GetPermissionsCommand(groupId, userId, getToken()));
    }

    /**
     * Method to perform a create-event request
     *
     * @param groupId   ID of the group of the event
     * @param title     The name of the event
     * @param timeStart The starting time of the event (unix time)
     * @param timeEnd   The finishing time of the event (unix time
     * @param position  The position of the event
     */
    public synchronized void createEvent(int groupId, String title,
                                         int creatorId, Date timeStart,
                                         Date timeEnd, Position position) {
        executeCommand(
                new CreateEventCommand(groupId, title, creatorId, timeStart,
                        timeEnd, position, getToken(), eventRepository));
    }

    public synchronized void editEvent(int groupId, String title, int creatorId,
                                       Date timeStart,
                                       Date timeEnd, Position position,
                                       int eventId) {
        executeCommand(
                new EditEventCommand(groupId, title, creatorId, timeStart,
                        timeEnd, position, eventId, getToken(), eventRepository));
    }

    public synchronized void joinEvent(int groupId, int eventId) {
        executeCommand(new JoinEventCommand(groupId, eventId, getToken()));
    }

    public synchronized void leaveEvent(int groupId, int eventId) {
        executeCommand(new LeaveEventCommand(groupId, eventId, getToken()));
    }

    public synchronized void removeEvent(int groupId, int eventId) {
        executeCommand(new RemoveEventCommand(groupId, eventId, getToken(),
                eventRepository));
    }

    public synchronized void createPoll(int groupId, String question,
                                        boolean isMultiChoice,
                                        Date timeVoteClose) {
        executeCommand(new CreatePollCommand(groupId, question,
                isMultiChoice, timeVoteClose, getToken()));
    }

    public synchronized void editPoll(int groupId, String question,
                                      boolean isMultiChoice,
                                      Date timeVoteClose, int id) {
        executeCommand(new EditPollCommand(groupId, question, isMultiChoice,
                timeVoteClose, id, getToken()));
    }

    public synchronized void addPollOption(int groupId, int pollId,
                                           long latitude, long longitude,
                                           Date timeStart, Date timeEnd) {
        executeCommand(new AddPollOptionCommand(groupId, pollId, latitude,
                longitude, timeStart, timeEnd, getToken()));
    }

    public synchronized void editPollOption(int groupId, int pollId,
                                            long latitude, long longitude,
                                            Date timeStart, Date timeEnd,
                                            int optionId) {
        executeCommand(
                new EditPollOptionCommand(groupId, pollId, latitude, longitude,
                        timeStart, timeEnd, optionId, getToken()));
    }

    public synchronized void voteForOption(int groupId, int pollId, int id) {
        executeCommand(new VoteForOptionCommand(groupId, pollId, id, getToken()));
    }

    public synchronized void withdrawVoteForOption(int groupId, int pollId,
                                                   int id) {
        executeCommand(
                new WithdrawVoteForOptionCommand(groupId, pollId, id, getToken()));
    }

    public synchronized void removePollOption(int groupId, int pollId, int id) {
        executeCommand(new RemovePollOptionCommand(groupId, pollId, id, getToken()));
    }

    public synchronized void removePoll(int groupId, int pollId) {
        executeCommand(new RemovePollCommand(groupId, pollId, getToken()));
    }

    /**
     * Method to perform a send-chat-message request
     *
     * @param groupId ID of the group receiving the message
     * @param message The chat message
     */

    public synchronized void sendChatMessage(int groupId, String message) {
        executeCommand(new SendChatMessageCommand(groupId, message, getToken(),
                chatRepository));
    }

    /**
     * Metod to get information about the users groups
     */
    public synchronized void listGroups() {
        executeCommand(new ListGroupsCommad(getToken()));
    }


    public synchronized void getGroupDetails(int groupId) {
        executeCommand(new GetGroupDetailsCommand(groupId, getToken(), userGroupRepository));
    }

    public synchronized void getUserDetails(int userId) {
        executeCommand(new GetUserDetailsCommand(userId, getToken(), userRepository));
    }

    public synchronized void getEventDetails(int eventId, int groupId) {
        executeCommand(new GetEventDetailsCommand(eventId, groupId,
                getToken(), eventRepository));
    }

    public synchronized void getPollDetails(int pollId, int groupId) {
        executeCommand(new GetPollDetailsCommand(pollId, groupId, getToken()));
    }

    public synchronized void requestPosition(int groupId, Date endTime) {
        executeCommand(new RequestPositionCommand(groupId, endTime, getToken()));
    }

    public synchronized void updatePosition(double latitude, double longitude,
                                            Date time) {
        executeCommand(
                new UpdatePositionCommand(latitude, longitude, time, getToken()));
    }

    public synchronized void publishPosition(int groupId, Date endTime) {
        executeCommand(new PublishPositionCommand(groupId, endTime,
                getToken(), userGroupRepository));
    }
}
