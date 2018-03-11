package org.pispeb.treffpunkt.client.data.networking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
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

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.networking.commands.*;
import org.pispeb.treffpunkt.client.data.repositories.ChatRepository;
import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.data.repositories.RepositorySet;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserRepository;
import org.pispeb.treffpunkt.client.view.login.LoginActivity;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.DeploymentException;

/**
 * This class provides methods to perform the known server requests.
 */

public class RequestEncoder implements ConnectionHandler.ResponseListener,
        CommandFailHandler {

    private static final int DISPLAY_ERROR_TOAST = 1;
    private static final int UPDATE_INTERVAL_MS = 10000;

    // mapper to convert from Pojos to Strings and vice versa
    private final ObjectMapper mapper;

    private ConnectionHandler connectionHandler;
    private boolean idle;

    private Handler bgHandler;
    private Handler uiHandler;

    private RepositorySet repositorySet;

    // Queue of commands waiting to be sent to Server
    private Queue<CommandJob> commands;

    private static RequestEncoder INSTANCE;

    private boolean updating;
    private Timer updateTimer;

    public static RequestEncoder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestEncoder();
        }

        return INSTANCE;
    }

    protected RequestEncoder() {
        commands = new LinkedList<>();
        idle = true;

        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature
                .FAIL_ON_MISSING_CREATOR_PROPERTIES);
        mapper.disable(DeserializationFeature
                .FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            connectionHandler
                    = new ConnectionHandler(getServerAddress(), this);
        } catch (URISyntaxException | IOException | DeploymentException e) {
            e.printStackTrace(); // TODO: TODONT
        }

        // Background Thread to execute network interaction on
        HandlerThread thread = new HandlerThread("bgt",
                HandlerThread.MIN_PRIORITY);
        thread.start();
        bgHandler = new Handler(thread.getLooper());

        updateTimer = new Timer();
        updating = false;

        // Handle to UIThread for displaying Toast messages
        uiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message.what == DISPLAY_ERROR_TOAST) {
                    Error error = (Error) message.obj;
                    TreffPunkt.showToast(error.getMessage());
                }
            }
        };

        nextCommand();
    }

    // must be called right after creating the encoder
    public void setRepos(ChatRepository chatRepository,
                         EventRepository eventRepository,
                         UserGroupRepository userGroupRepository,
                         UserRepository userRepository) {
        this.repositorySet = new RepositorySet(chatRepository, eventRepository,
                userGroupRepository, userRepository);
    }

    public void startRequestUpdates() {
        RequestEncoder thisEnc = this;
        this.updateTimer = new Timer();
        // request Updates periodically
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (thisEnc.idle) {
                    requestUpdates();
                }
            }
        }, 0, UPDATE_INTERVAL_MS);
        updating = true;
    }

    public void stopRequestUpdates() {
        updateTimer.cancel();
        updating = false;
    }

    /**
     * Add a command to the queue
     * If the RequestEncoder is not currently expecting any response (i.e.
     * all former commands are dealt with) send the oldest request to the server
     *
     * @param command Command to be executed as soon as all pending commands
     *                receive a response
     */
    private synchronized void executeCommand(AbstractCommand command) {
        commands.add(new CommandJob(command, this, false));
        Log.i("Encoder", "Command queue size: " + commands.size());
        nextCommand();
    }

    private synchronized void executeCommand(AbstractCommand command,
                                             CommandFailHandler failHandler,
                                             boolean abortOnCantConnect) {
        commands.add(new CommandJob(command, failHandler, abortOnCantConnect));
        Log.i("Encoder", "Command queue size: " + commands.size());
        nextCommand();
    }

    /**
     * Convert a Request into a String using the json mapper
     * and asynchronously passes it to the {@link ConnectionHandler}.
     *
     * @param request Json-object of the next command's request
     */
    private synchronized void sendRequest(AbstractRequest request,
                                          boolean abortOnCantConnect) {
        String message;
        try {
            message = mapper.writeValueAsString(request);
            sendToCH(message, abortOnCantConnect);
        } catch (JsonProcessingException e) {
            // This would mean, that the internal request encoding is messed
            // up, which would be very bad indeed!
            e.printStackTrace();
        }
    }

    /**
     * Asynchronously pass a message to the {@link ConnectionHandler} to send
     * it to the server
     *
     * @param message content of that message (in correct command format)
     */
    protected synchronized void sendToCH(String message,
                                         boolean abortOnCantConnect) {
        bgHandler.post(() -> {
            // if disconnected before, reconnect
            if (connectionHandler == null)
                try {
                    connectionHandler
                            = new ConnectionHandler(getServerAddress(), this);
                } catch (URISyntaxException | IOException
                        | DeploymentException e) {
                    e.printStackTrace();
                }
            connectionHandler.sendMessage(message, abortOnCantConnect);
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

    private String getServerAddress() {
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        boolean defaultAddress
                = pref.getBoolean(
                ctx.getString(R.string.key_using_default_server), true);

        if (defaultAddress)
            return TreffPunkt.STANDARD_URL;
        else
            return pref.getString(ctx.getString(R.string.key_server_address),
                    "");
    }

    /**
     * Asynchronously executes the next command
     */
    private void nextCommand() {
        if (!commands.isEmpty()) {
            sendRequest(commands.peek().command.getRequest(),
                    commands.peek().abortOnCantConnect);
        } else {
            idle = true;
        }
    }

    @Override
    public void onResponse(String responseString) {
        // no commands waiting for response
        if (commands.isEmpty()) {
            //should never happen as long as server is working correctly
            return;
        }
        CommandJob job = commands.poll();
        AbstractCommand c = job.command;
        CommandFailHandler failHandler = job.failHandler;
        try {
            ErrorResponse errorResponse = mapper
                    .readValue(responseString, ErrorResponse.class);
            Log.i("RequestEncoder", "Error");
            Error error = Error.getValue(errorResponse.error);
            failHandler.onFail(error);
        } catch (IOException e) {
            // it is no Error
            try {
                AbstractResponse response = mapper.readValue(responseString,
                        c.getResponseClass());
                c.onResponse(response);
                if (!updating) {
                    startRequestUpdates();
                }
            } catch (IOException ex) {
                // This would mean, that the internal request encoding is messed
                // up, which would be very bad indeed!
                ex.printStackTrace();
            }
        }

        nextCommand();
    }

    @Override
    public void onTimeout() {
        // discard command on top of the queue and execute next one
        commands.poll();
        nextCommand();
    }

    @Override
    public void onCantConnect() {
        // discard current command and display error message
        CommandFailHandler failHandler = commands.poll().failHandler;
        nextCommand();
        failHandler.onFail(Error.CANT_CONNECT);
    }

    /**
     * Json-description of an error response as the server returns it in case
     * of Syntax, Database or any other error in the given command
     */
    public static class ErrorResponse extends AbstractResponse {
        public final int error;

        public ErrorResponse(@JsonProperty("error") int error) {
            this.error = error;
        }
    }

    @Override
    public void onFail(Error error) {
        Log.e("RequestEncoder", error.getCode() +
                ": " + error.getMessage());
        if (error.isUserRelevant()) {
            Message message = uiHandler.obtainMessage(DISPLAY_ERROR_TOAST,
                    error);
            message.sendToTarget();
        } else {
            Message message = uiHandler.obtainMessage(DISPLAY_ERROR_TOAST,
                    Error.INTERNAL_ERROR);
            message.sendToTarget();
        }

        if (error == Error.TOKEN_INV) {
            // wipe token
            Context appctx = TreffPunkt.getAppContext();
            SharedPreferences pref = PreferenceManager
                    .getDefaultSharedPreferences(appctx);
            pref.edit().remove(appctx
                    .getString(R.string.key_token)).apply();
            stopRequestUpdates();

            // restart App
            Intent restartApp = new Intent(appctx, LoginActivity.class);
            restartApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            appctx.startActivity(restartApp);
        }
    }

    /**
     * When exiting the App, clean up any network connections
     */
    public void closeConnection() {
        // TODO check for Service still running
        // stop update timer
        bgHandler.post(() -> {
            stopRequestUpdates();
            connectionHandler.disconnect();
            connectionHandler = null;
        });
    }

    /**
     * Method to perform a register request
     *
     * @param username .
     * @param password .
     * @param email
     */
    public synchronized void register(String username, String password,
                                      String email,
                                      CommandFailHandler failHandler) {
        executeCommand(new RegisterCommand(username, password, email),
                failHandler, true);
    }

    /**
     * Method to perform a login request
     *
     * @param username .
     * @param password .
     */
    public synchronized void login(String username, String password,
                                   CommandFailHandler failHandler) {
        executeCommand(new LoginCommand(username, password),
                failHandler, true);
    }


    /**
     * Edit the username
     *
     * @param username the new username
     */
    public synchronized void editUsername(String username) {
        executeCommand(new EditUsernameCommand(username, getToken()));
    }

    /**
     * Change the users email address
     *
     * @param email the new email
     */
    public synchronized void editEmail(String email) {
        executeCommand(new EditEmailCommand(email, getToken()));
    }

    /**
     * Change the users password directly
     *
     * @param password    the current password
     * @param newPassword the new password
     */
    public synchronized void editPassword(String password, String newPassword) {
        executeCommand(
                new EditPasswordCommand(password, newPassword, getToken()));
    }

    /**
     * Send a code to the users email in order to reset the password
     *
     * @param email the users email
     */
    public synchronized void resetPassword(String email) {
        executeCommand(new ResetPasswordCommand(email));
    }

    /**
     * Reset the users password after requesting a reset via email
     *
     * @param code     the validation code of the user
     * @param password the new password
     */

    public synchronized void resetPasswordConfirm(String code,
                                                  String password) {
        executeCommand(new ResetPasswordConfirmCommand(code, password));
    }

    /**
     * Delete the Account of the user that is currently logged in
     *
     * @param password the password of the user
     */
    public synchronized void deleteAccount(String password) {
        executeCommand(new DeleteAccountCommand(password, getToken()));
    }

    /**
     * Method to perform an send-contact-request request
     *
     * @param userId Contact to be added to the friend list
     */
    public synchronized void sendContactRequest(int userId) {
        executeCommand(new SendContactRequestCommand(userId, getToken(),
                repositorySet.userRepository));
    }

    /**
     * Send a contact request to the given user
     * Determines the id of the given user before requesting
     *
     * @param userName Name of the user to be added to the friend list
     */
    public synchronized void sendContactRequest(String userName) {
        executeCommand(new GetUserIdCommand(userName, getToken(),
                repositorySet.userRepository, this));
    }

    public synchronized void cancelContactRequest(int userId) {
        executeCommand(new CancelContactRequestCommand(userId, getToken(),
                repositorySet.userRepository));
    }

    /**
     * Accept a contact request from a given user
     *
     * @param userId user from which the request originated
     */
    public synchronized void acceptContactRequest(int userId) {
        executeCommand(new AcceptContactRequestCommand(userId, getToken(),
                repositorySet.userRepository));
    }

    /**
     * Reject a contact request
     *
     * @param userId user from which the request originated
     */
    public synchronized void rejectContactRequest(int userId) {
        executeCommand(new RejectContactRequestCommand(userId, getToken(),
                repositorySet.userRepository));
    }

    public synchronized void removeContact(int userId) {
        executeCommand(
                new RemoveContactCommand(userId, getToken(),
                        repositorySet.userRepository));
    }

    /**
     * update the list of contact as well as requests
     */
    public synchronized void getContactList() {
        executeCommand(new GetContactListCommand(getToken(),
                repositorySet.userRepository, this));
    }

    /**
     * block an account
     *
     * @param userId
     */
    public synchronized void blockAccount(int userId) {
        executeCommand(
                new BlockAccountCommand(userId, getToken(),
                        repositorySet.userRepository));
    }

    /**
     * unblock another Account
     *
     * @param userId The user to be unblocked
     */
    public synchronized void unblockAccount(int userId) {
        executeCommand(
                new UnblockAccountCommand(userId, getToken(),
                        repositorySet.userRepository));
    }

    /**
     * Create a new group with a list of members
     *
     * @param groupName Name of the new group
     * @param memberIds Array of the ID's of the members
     */
    public synchronized void createGroup(String groupName, int[] memberIds) {
        executeCommand(new CreateGroupCommand(groupName, memberIds, getToken(),
                repositorySet.userGroupRepository));
    }

    /**
     * Edit the name of the given group
     *
     * @param groupId ID of the group
     */
    public synchronized void editGroup(int groupId, String groupName) {
        executeCommand(new EditGroupCommand(groupId, groupName, getToken(),
                repositorySet.userGroupRepository));
    }

    /**
     * Add users to a given group
     *
     * @param groupId    ID of the group
     * @param newMembers ID of the new member
     * @return true if the request was successful, false if not
     */
    public synchronized void addGroupMembers(int groupId, int[] newMembers) {
        executeCommand(
                new AddGroupMembersCommand(groupId, newMembers, getToken(),
                        repositorySet.userGroupRepository));
    }

    /**
     * Remove users from a specified group
     *
     * @param groupId The ID of the group
     * @param members The ID of the member to be removed
     */
    public synchronized void removeGroupMembers(int groupId, int[] members) {
        executeCommand(
                new RemoveGroupMembersCommand(groupId, members, getToken(),
                        repositorySet.userGroupRepository));
    }

    /**
     * Leave the specified group
     *
     * @param groupId The ID of the group
     */
    public synchronized void leaveGroup(int groupId) {
        executeCommand(new LeaveGroupCommand(groupId, getToken(),
                repositorySet.userGroupRepository));
    }

    public synchronized void getPermissions(int groupId, int userId) {
        executeCommand(new GetPermissionsCommand(groupId, userId, getToken()));
    }

    /**
     * Method to add an event
     *
     * @param groupId   ID of the group of the event
     * @param title     The name of the event
     * @param timeStart The starting time of the event (unix time)
     * @param timeEnd   The finishing time of the event (unix time
     * @param location  The location of the event
     */
    public synchronized void createEvent(int groupId, String title,
                                         int creatorId, Date timeStart,
                                         Date timeEnd, Location location) {
        executeCommand(
                new CreateEventCommand(groupId, title, creatorId, timeStart,
                        timeEnd, location, getToken(),
                        repositorySet.eventRepository));
    }

    public synchronized void editEvent(int groupId, String title, int creatorId,
                                       Date timeStart, Date timeEnd,
                                       Location location, int eventId) {
        executeCommand(
                new EditEventCommand(groupId, title, creatorId, timeStart,
                        timeEnd, location, eventId, getToken(),
                        repositorySet.eventRepository));
    }

    public synchronized void joinEvent(int groupId, int eventId) {
        executeCommand(new JoinEventCommand(groupId, eventId, getToken()));
    }

    public synchronized void leaveEvent(int groupId, int eventId) {
        executeCommand(new LeaveEventCommand(groupId, eventId, getToken()));
    }

    public synchronized void removeEvent(int groupId, int eventId) {
        executeCommand(new RemoveEventCommand(groupId, eventId, getToken(),
                repositorySet.eventRepository));
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
        executeCommand(
                new VoteForOptionCommand(groupId, pollId, id, getToken()));
    }

    public synchronized void withdrawVoteForOption(int groupId, int pollId,
                                                   int id) {
        executeCommand(
                new WithdrawVoteForOptionCommand(groupId, pollId, id,
                        getToken()));
    }

    public synchronized void removePollOption(int groupId, int pollId, int id) {
        executeCommand(
                new RemovePollOptionCommand(groupId, pollId, id, getToken()));
    }

    public synchronized void removePoll(int groupId, int pollId) {
        executeCommand(new RemovePollCommand(groupId, pollId, getToken()));
    }

    /**
     * Method to send a chat message in a given group
     *
     * @param groupId ID of the group receiving the message
     * @param message The chat message
     */

    public synchronized void sendChatMessage(int groupId, String message) {
        executeCommand(new SendChatMessageCommand(groupId, message, getToken(),
                repositorySet.chatRepository));
    }

    /**
     * Method to get information about the users groups
     */
    public synchronized void listGroups() {
        executeCommand(new ListGroupsCommand(getToken(),
                repositorySet.userGroupRepository, this));
    }


    public synchronized void getGroupDetails(int groupId) {
        executeCommand(new GetGroupDetailsCommand(groupId, getToken(),
                repositorySet.userGroupRepository,
                repositorySet.eventRepository, this));
    }

    public synchronized void getUserDetails(int userId) {
        executeCommand(
                new GetUserDetailsCommand(userId, getToken(),
                        repositorySet.userRepository));
    }

    public synchronized void getEventDetails(int eventId, int groupId) {
        executeCommand(new GetEventDetailsCommand(eventId, groupId,
                getToken(), repositorySet.eventRepository));
    }

    public synchronized void getPollDetails(int pollId, int groupId) {
        executeCommand(new GetPollDetailsCommand(pollId, groupId, getToken()));
    }

    public synchronized void requestPosition(int groupId, Date endTime) {
        executeCommand(
                new RequestPositionCommand(groupId, endTime, getToken()));
    }

    /**
     * send the most recent location to the server
     *
     * @param latitude  latitude
     * @param longitude and longitude of the users position
     * @param time      time the position was measured at
     */
    public synchronized void updatePosition(double latitude, double longitude,
                                            Date time) {
        executeCommand(
                new UpdatePositionCommand(latitude, longitude, time,
                        getToken()));
    }

    /**
     * start a transmission of the users location to the group lasting until
     * the time given
     *
     * @param groupId grop with which the user shares his location
     * @param endTime time at which the transmission is supposed to end
     */
    public synchronized void publishPosition(int groupId, Date endTime) {
        executeCommand(new PublishPositionCommand(groupId, endTime,
                getToken(), repositorySet.userGroupRepository));
    }

    public synchronized void requestUpdates() {
        executeCommand(new RequestUpdatesCommand(getToken(), repositorySet, mapper));
    }

}