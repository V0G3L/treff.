package org.pispeb.treff_server;

/**
 * Class to decode and handle JSON-encoded requests
 */
public class RequestHandler {

    public RequestHandler () {

    }

    /**
     * Takes a JSON-encoded request, decodes and executes it,
     * and returns a JSON-encoded response
     *
     * @param request JSON-encode Request
     * @return JSON-encoded Answer
     */
    public String handleRequest(String request) {
        // decode (and check syntax)
        // check permissions and compatibility with current state
        // send request to Database
        // encode return value
        return null;
    }
}
