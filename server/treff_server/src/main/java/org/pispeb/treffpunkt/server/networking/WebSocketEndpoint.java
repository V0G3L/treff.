package org.pispeb.treffpunkt.server.networking;

import org.pispeb.treffpunkt.server.Server;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author tim
 */
@ServerEndpoint("/ws")
public class WebSocketEndpoint {
    private HashSet<Session> persistentConnections = new HashSet<>();
    private Server server;
    private final Logger logger = Logger.getLogger("WS Endpoint");

    public WebSocketEndpoint() {
        this.server = Server.getInstance();
    }

    /**
     * Used for testing.
     *
     * @param server
     */
    public WebSocketEndpoint(Server server) {
        this.server = server;
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        logger.info(String.format("RECEIVED on %s: %s", session.getId(),
                message));
        // Clients shouldn't to send messages on persistent Connections
        // so they don't get any answer
        if (persistentConnections.contains(session))
            return null;

        // Handle the request
        Response response = server.getRequestHandler().handleRequest(message);

        // send back the answer
        if (response.requestedPersistentConnection) {
            persistentConnections.add(session);
            // TODO: implement
//            new PersistentConnection(session,
//                    , response.accountID);
            return "{}";
        } else {
            logger.info(String.format("SENDING on %s: %s", session.getId(),
                    response.responseString));
            return response.responseString;
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info(String.format("OPENED session %s", session.getId()));
    }

    @OnClose
    public void onClose(Session session) {
        logger.info(String.format("CLOSED session %s", session.getId()));
    }

    @OnError
    public void onError(Throwable error) {
        if (error instanceof EOFException) {
            logger.info("Some client didn't close the connection properly.");
        } else {
            logger.info(String.format("ERROR\nMessage: %s\nStack trace:\n%s",
                    error.getMessage(),
                    Arrays.stream(error.getStackTrace())
                            .map(StackTraceElement::toString)
                            .collect(Collectors.joining("\n"))));
        }
    }

//    @OnError
//    public void onError() { }
}
