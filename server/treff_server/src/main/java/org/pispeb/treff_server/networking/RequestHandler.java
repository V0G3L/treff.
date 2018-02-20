package org.pispeb.treff_server.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.pispeb.treff_server.commands.*;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.ProgrammingError;
import org.pispeb.treff_server.interfaces.AccountManager;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

/**
 * Class to decode and handle JSON-encoded requests
 */
public class RequestHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        // Do not fail on unknown properties. Important because cmd is extracted
        // seperately.
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // Allow serialization of empty CommandOutputs
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // Make sure mapping fails if a property is missing, we don't want
        // Jackson to just fill in defaults
        mapper.enable(
                DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
    }

    private final AccountManager accountManager;

    public RequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
        registerAllCommands();
    }

    public Response handleRequest(String requestString) {

        JsonObject request = Json
                .createReader(new StringReader(requestString))
                .readObject();

        // if cmd property is missing, return a syntax error message
        if (!request.containsKey("cmd"))
            return toErrorResponse(ErrorCode.SYNTAXINVALID);

        // if cmdString doesn't map to a command class, return an
        // unknown command error message
        String cmdString = request.getString("cmd");
        if (cmdString.equals("request-persistent-connection")) {
            if (accountManager
                    .getAccountByLoginToken(request.getString("token"))
                    == null)
                return toErrorResponse(ErrorCode.TOKENINVALID);
            return new Response(request.getString("id"));
        }

        Class<? extends AbstractCommand> commandClass
                = AbstractCommand.getCommandByStringIdentifier(cmdString);
        if (commandClass == null)
            return toErrorResponse(ErrorCode.UNKNOWN_COMMAND);

        // instantiate command
        AbstractCommand command = null;
        try {
            command = commandClass
                    .getConstructor(AccountManager.class, ObjectMapper.class)
                    .newInstance(accountManager, mapper);
        } catch (InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            // This should only happen when a command class uses a
            // non-standard constructor
            throw new ProgrammingError();
        }

        String outputString = command.execute(requestString);
        return new Response(outputString);
    }

    private Response toErrorResponse(ErrorCode errorCode) {
        try {
            return new Response(mapper.writeValueAsString(
                    new ErrorOutput(errorCode)));
        } catch (JsonProcessingException e) {
            throw new ProgrammingError();
        }
    }

    private void registerAllCommands() {
        // all hail strings replacements
        // and screw tomcat for not executing our static blocks
        AbstractCommand.registerCommand("accept-contact-request", AcceptContactRequestCommand.class);
        AbstractCommand.registerCommand("add-group-members", AddGroupMembersCommand.class);
        AbstractCommand.registerCommand("add-poll-option", AddPollOptionCommand.class);
        AbstractCommand.registerCommand("block-account", BlockAccountCommand.class);
        AbstractCommand.registerCommand("cancel-contact-request", CancelContactRequestCommand.class);
        AbstractCommand.registerCommand("create-event", CreateEventCommand.class);
        AbstractCommand.registerCommand("create-group", CreateGroupCommand.class);
        AbstractCommand.registerCommand("create-poll", CreatePollCommand.class);
        AbstractCommand.registerCommand("delete-account", DeleteAccountCommand.class);
        AbstractCommand.registerCommand("edit-email", EditEmailCommand.class);
        AbstractCommand.registerCommand("edit-event", EditEventCommand.class);
        AbstractCommand.registerCommand("edit-group", EditGroupCommand.class);
        AbstractCommand.registerCommand("edit-membership", EditMembershipCommand.class);
        AbstractCommand.registerCommand("edit-password", EditPasswordCommand.class);
        AbstractCommand.registerCommand("edit-poll", EditPollCommand.class);
        AbstractCommand.registerCommand("edit-poll-option", EditPollOptionCommand.class);
        AbstractCommand.registerCommand("edit-username", EditUsernameCommand.class);
        AbstractCommand.registerCommand("get-contact-list", GetContactListCommand.class);
        AbstractCommand.registerCommand("get-event-details", GetEventDetailsCommand.class);
        AbstractCommand.registerCommand("get-group-details", GetGroupDetailsCommand.class);
        AbstractCommand.registerCommand("get-membership-details", GetMembershipDetailsCommand.class);
        AbstractCommand.registerCommand("get-poll-details", GetPollDetailsCommand.class);
        AbstractCommand.registerCommand("get-user-details", GetUserDetailsCommand.class);
        AbstractCommand.registerCommand("get-user-id", GetUserIdCommand.class);
        AbstractCommand.registerCommand("join-event", JoinEventCommand.class);
        AbstractCommand.registerCommand("leave-event", LeaveEventCommand.class);
        AbstractCommand.registerCommand("list-groups", ListGroupsCommand.class);
        AbstractCommand.registerCommand("login", LoginCommand.class);
        AbstractCommand.registerCommand("publish-position", PublishPositionCommand.class);
        AbstractCommand.registerCommand("register", RegisterCommand.class);
        AbstractCommand.registerCommand("reject-contact-request", RejectContactRequestCommand.class);
        AbstractCommand.registerCommand("remove-contact", RemoveContactCommand.class);
        AbstractCommand.registerCommand("remove-event", RemoveEventCommand.class);
        AbstractCommand.registerCommand("remove-group-members", RemoveGroupMembersCommand.class);
        AbstractCommand.registerCommand("remove-poll", RemovePollCommand.class);
        AbstractCommand.registerCommand("remove-poll-option", RemovePollOptionCommand.class);
        AbstractCommand.registerCommand("request-position", RequestPositionCommand.class);
        AbstractCommand.registerCommand("request-updates", RequestUpdatesCommand.class);
        AbstractCommand.registerCommand("reset-password", RegisterCommand.class);
        AbstractCommand.registerCommand("reset-password-confirm", ResetPasswordConfirmCommand.class);
        AbstractCommand.registerCommand("send-chat-message", SendChatMessageCommand.class);
        AbstractCommand.registerCommand("send-contact-request", SendContactRequestCommand.class);
        AbstractCommand.registerCommand("unblock-account", UnblockAccountCommand.class);
        AbstractCommand.registerCommand("update-position", UpdatePositionCommand.class);
        AbstractCommand.registerCommand("vote-for-option", VoteForOptionCommand.class);
        AbstractCommand.registerCommand("withdraw-vote-for-option", WithdrawVoteForOptionCommand.class);
    }
}
