package org.pispeb.treff_server;

/**
 * @author tim
 */
public class RequestHandlerResponse {

    public final String responseString;

    public final boolean requestedPersistentConnection;
    public final int accountID;

    public RequestHandlerResponse(String responseString, boolean
            requestedPersistentConnection, int accountID) {
        this.responseString = responseString;
        this.requestedPersistentConnection = requestedPersistentConnection;
        this.accountID = accountID;
    }
}
