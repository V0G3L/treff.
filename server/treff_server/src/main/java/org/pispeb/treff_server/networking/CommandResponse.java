package org.pispeb.treff_server.networking;

import javax.json.JsonObject;

/**
 * The Response of an API-command
 */
public class CommandResponse {

    /**
     * the status code of the command
     * the occured errorcode, 000 if no error occured
     */
    private StatusCode statusCode;
    /**
     * the response of the command encoded as a JsonObject
     * empty JsonObject if an error occured
     */
    private JsonObject response;

    public CommandResponse(StatusCode statusCode, JsonObject response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public JsonObject getResponse() {
        return response;
    }
}
