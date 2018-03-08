package org.pispeb.treff_server.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.pispeb.treff_server.commands.*;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.DuplicateCommandIdentifier;
import org.pispeb.treff_server.exceptions.ProgrammingException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String, Class<? extends AbstractCommand>>
            availableCommands = new HashMap<>();

    private final AccountManager accountManager;
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
        registerAllCommands();
    }

    public Response handleRequest(String requestString) {
        try { // catch-all for internal server errors

            // try converting input to JsonObject, abort on exception
            JsonObject request;
            try {
                request = Json
                        .createReader(new StringReader(requestString))
                        .readObject();
            } catch (JsonParsingException e) {
                return toErrorResponse(ErrorCode.SYNTAXINVALID);
            }

            // if cmd property is missing, return a syntax error message
            if (!request.containsKey("cmd"))
                return toErrorResponse(ErrorCode.SYNTAXINVALID);

            // if cmdString doesn't map to a command class, return an
            // unknown command error message
            String cmdString = request.getString("cmd");
            if (cmdString.equals("request-persistent-connection")) {
                Account actingAccount = accountManager
                        .getAccountByLoginToken(request.getString("token"));
                if (actingAccount == null)
                    return toErrorResponse(ErrorCode.TOKENINVALID);
                return new Response(actingAccount.getID());
            }

            Class<? extends AbstractCommand> commandClass
                    = getCommandByStringIdentifier(cmdString);
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
                throw new ProgrammingException();
            }

            String outputString = command.execute(requestString);
            return new Response(outputString);
        } catch (Exception e) {
            logger.error("Internal server error on request\n\n" +
                    "{}\n\n" +
                    "Error message:\n" +
                    "{}", requestString, e.getMessage());
            return new Response(Json.createObjectBuilder()
                    .add("error", ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                    .build()
                    .toString());
        }
    }

    private Response toErrorResponse(ErrorCode errorCode) {
        try {
            return new Response(mapper.writeValueAsString(
                    new ErrorOutput(errorCode)));
        } catch (JsonProcessingException e) {
            throw new ProgrammingException();
        }
    }

    private void registerAllCommands() {
        // all hail strings replacements
        // and screw tomcat for not executing our static blocks
        registerCommand("accept-contact-request",
                AcceptContactRequestCommand.class);
        registerCommand("add-group-members", AddGroupMembersCommand.class);
        registerCommand("add-poll-option", AddPollOptionCommand.class);
        registerCommand("block-account", BlockAccountCommand.class);
        registerCommand("cancel-contact-request",
                CancelContactRequestCommand.class);
        registerCommand("create-event", CreateEventCommand.class);
        registerCommand("create-group", CreateGroupCommand.class);
        registerCommand("create-poll", CreatePollCommand.class);
        registerCommand("delete-account", DeleteAccountCommand.class);
        registerCommand("edit-email", EditEmailCommand.class);
        registerCommand("edit-event", EditEventCommand.class);
        registerCommand("edit-group", EditGroupCommand.class);
        registerCommand("edit-membership", EditMembershipCommand.class);
        registerCommand("edit-password", EditPasswordCommand.class);
        registerCommand("edit-poll", EditPollCommand.class);
        registerCommand("edit-poll-option", EditPollOptionCommand.class);
        registerCommand("edit-username", EditUsernameCommand.class);
        registerCommand("get-contact-list", GetContactListCommand.class);
        registerCommand("get-event-details", GetEventDetailsCommand.class);
        registerCommand("get-group-details", GetGroupDetailsCommand.class);
        registerCommand("get-membership-details",
                GetMembershipDetailsCommand.class);
        registerCommand("get-poll-details", GetPollDetailsCommand.class);
        registerCommand("get-user-details", GetUserDetailsCommand.class);
        registerCommand("get-user-id", GetUserIdCommand.class);
        registerCommand("join-event", JoinEventCommand.class);
        registerCommand("leave-event", LeaveEventCommand.class);
        registerCommand("list-groups", ListGroupsCommand.class);
        registerCommand("login", LoginCommand.class);
        registerCommand("publish-position", PublishPositionCommand.class);
        registerCommand("register", RegisterCommand.class);
        registerCommand("reject-contact-request",
                RejectContactRequestCommand.class);
        registerCommand("remove-contact", RemoveContactCommand.class);
        registerCommand("remove-event", RemoveEventCommand.class);
        registerCommand("remove-group-members",
                RemoveGroupMembersCommand.class);
        registerCommand("remove-poll", RemovePollCommand.class);
        registerCommand("remove-poll-option", RemovePollOptionCommand.class);
        registerCommand("request-position", RequestPositionCommand.class);
        registerCommand("request-updates", RequestUpdatesCommand.class);
        registerCommand("reset-password", RegisterCommand.class);
        registerCommand("reset-password-confirm",
                ResetPasswordConfirmCommand.class);
        registerCommand("send-chat-message", SendChatMessageCommand.class);
        registerCommand("send-contact-request",
                SendContactRequestCommand.class);
        registerCommand("unblock-account", UnblockAccountCommand.class);
        registerCommand("update-position", UpdatePositionCommand.class);
        registerCommand("vote-for-option", VoteForOptionCommand.class);
        registerCommand("withdraw-vote-for-option",
                WithdrawVoteForOptionCommand.class);
    }

    private void registerCommand(String stringIdentifier,
                                       Class<? extends AbstractCommand>
                                               command) {
        if (availableCommands.containsKey(stringIdentifier))
            throw new DuplicateCommandIdentifier(stringIdentifier);
        else
            availableCommands.put(stringIdentifier, command);
    }

    private Class<? extends AbstractCommand> getCommandByStringIdentifier(
            String stringIdentifier) {
        return availableCommands.get(stringIdentifier);
    }
}
