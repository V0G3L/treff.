package org.pispeb.treff_server.networking;

import javax.json.JsonObject;

import static org.pispeb.treff_server.networking.StatusCode.SUCCESSFUL;

/**
 * The Response of an API-command
 */
public class CommandResponse {

    /**
     * the status code of the command
     * the occured errorcode, 000 if no error occured
     */
    public final StatusCode statusCode;
    /**
     * the response of the command encoded as a JsonObject
     * empty JsonObject if an error occured
     */
    public final JsonObject response;

    /**
     * Constructs a new CommandReponse representing a successful execution of
     * a command that produced an output. {@link #statusCode} will be set to
     * {@link StatusCode#SUCCESSFUL}.
     *
     * @param response The output produced by the executed command
     */
    public CommandResponse(JsonObject response) {
        this.statusCode = SUCCESSFUL;
        this.response = response;
    }

    /**
     * Construct a new CommandResponse representing either a successful
     * execution of a command that didn't produce output or an unsuccessful
     * execution.
     *
     * @param statusCode {@link StatusCode#SUCCESSFUL} if the execution was
     *                   successful and didn't produce output. Otherwise the
     *                   {@link StatusCode} that represents the error that
     *                   occurred.
     */
    public CommandResponse(StatusCode statusCode) {
        this.statusCode = statusCode;
        this.response = null;
    }
}
