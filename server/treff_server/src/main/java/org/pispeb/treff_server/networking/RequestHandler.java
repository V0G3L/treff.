package org.pispeb.treff_server.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.pispeb.treff_server.commands.AbstractCommand;
import org.pispeb.treff_server.commands.io.ErrorOutput;
import org.pispeb.treff_server.exceptions.RequestHandlerAlreadyRan;
import org.pispeb.treff_server.interfaces.AccountManager;

import java.io.IOException;

/**
 * Class to decode and handle JSON-encoded requests
 */
public class RequestHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final String requestString;
    private final AccountManager accountManager;
    private boolean didRun = false;

    /**
     * Creates a RequestHandler for a JSON-encoded requestString.
     *
     * @param requestString        The JSON-encoded requestString
     * @param accountManager The database entry-point
     */
    public RequestHandler(String requestString, AccountManager accountManager) {
        this.requestString = requestString;
        this.accountManager = accountManager;
    }

    /**
     * May only be run once on the same {@link RequestHandler}.
     *
     * @return JSON-encoded response
     * @throws RequestHandlerAlreadyRan if the requestString of this RequestHandler
     *                                  was handled via {@link #run()}
     */
    public Response run() throws RequestHandlerAlreadyRan {
        if (didRun)
            throw new RequestHandlerAlreadyRan();
        didRun = true;

        // Do not fail on unknown properties. Important because we're only
        // extracting the cmd for now.
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        Request request = null;
        try {
            request = mapper.readValue(this.requestString, Request.class);
        } catch (IOException e) {
            try {
                return new Response(mapper.writeValueAsString(
                        new ErrorOutput(ErrorCode.SYNTAXINVALID)));
            } catch (JsonProcessingException e1) {
                throw new AssertionError("This shouldn't happen."); // TODO: really?
            }
        }

        AbstractCommand command = request.getCommandObject(accountManager);

        // if no command object was created, command is unknown, return error
        if (command == null) {
            try {
                return new Response(mapper.writeValueAsString(
                        new ErrorOutput(ErrorCode.UNKNOWN_COMMAND)));
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }

        String outputString = command.execute(requestString);
        return new Response(outputString);
    }
}
