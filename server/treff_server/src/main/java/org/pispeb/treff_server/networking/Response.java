package org.pispeb.treff_server.networking;

/**
 * @author tim
 */
public class Response {

    public final String responseString;

    public final boolean requestedPersistentConnection;
    public final int accountID;

    public Response(String responseString) {
        this.responseString = responseString;
        this.requestedPersistentConnection = false;
        this.accountID = -1;
    }

    public Response(int accountID) {
        this.responseString = null;
        this.requestedPersistentConnection = true;
        this.accountID = accountID;
    }
}
