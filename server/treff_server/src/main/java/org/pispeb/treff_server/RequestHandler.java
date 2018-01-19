package org.pispeb.treff_server;

import org.pispeb.treff_server.exceptions.RequestHandlerAlreadyRan;
import org.pispeb.treff_server.interfaces.AccountManager;

/**
 * Class to decode and handle JSON-encoded requests
 */
public class RequestHandler {

    private final String request;
    private final AccountManager accountManager;
    private final boolean didRun = false;

    /**
     * Creates a RequestHandler for a JSON-encoded request.
     * @param request The JSON-encoded request
     * @param accountManager The database entry-point
     */
    public RequestHandler(String request, AccountManager accountManager) {
        this.request = request;
        this.accountManager = accountManager;
    }

    /**
     * Decodes and executes the request
     * and returns either a JSON-encoded response or
     * {@link #PERSISTENT_CONNECTION_REQUEST_PREFIX} followed by the user id
     * if a request for a
     * {@link org.pispeb.treff_server.update_notifier.PersistentConnection}
     * was made.
     * May only be run once on the same {@link RequestHandler}.
     *
     * @return JSON-encoded response
     * @throws RequestHandlerAlreadyRan if the request of this RequestHandler
     * was handled via {@link #run()}
     */
    public RequestHandlerResponse run() throws RequestHandlerAlreadyRan {
        // decode (and check syntax)
        // check permissions and compatibility with current state
        // send request to Database
        // encode return value


        return null;
    }
}
