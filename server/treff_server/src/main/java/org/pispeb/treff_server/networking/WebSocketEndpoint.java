package org.pispeb.treff_server.networking;

import org.pispeb.treff_server.Server;
import org.pispeb.treff_server.update_notifier.PersistentConnection;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;

/**
 * @author tim
 */
@ServerEndpoint("/ws")
public class WebSocketEndpoint {
    private HashSet<Session> persistentConnections = new HashSet<>();

    @OnMessage
    public String onMessage(String message, Session session) {
        // Clients shouldn't to send messages on persistent Connections
        // so they don't get any answer
        if (persistentConnections.contains(session))
            return null;

        // Handle the request
        Server server = Server.getInstance();
        Response response = server.getRequestHandler().handleRequest(message);

        // send back the answer
        if (response.requestedPersistentConnection) {
            persistentConnections.add(session);
            new PersistentConnection(session,
                    server.getAccountManager(),response.accountID);
            return "{}";
        } else {
            return response.responseString;
        }
    }
}
