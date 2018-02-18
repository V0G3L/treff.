package org.pispeb.treff_server.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.pispeb.treff_server.commands.AbstractCommand;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.ProgrammingError;
import org.pispeb.treff_server.exceptions.RequestHandlerAlreadyRan;
import org.pispeb.treff_server.interfaces.AccountManager;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
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
}
