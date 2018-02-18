package org.pispeb.treff_server.networking;

import org.pispeb.treff_server.Server;
import org.pispeb.treff_server.update_notifier.PersistentConnection;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author tim
 */
@ServerEndpoint("/ws")
public class WebSocketEndpoint {

    @OnMessage
    public String onMessage(String message, Session session) {
        Server server = Server.getInstance();
        Response response = server.getRequestHandler().handleRequest(message);

        if (response.requestedPersistentConnection) {
            new PersistentConnection(session,
                    server.getAccountManager(),response.accountID);
            return "{}";
        } else {
            return response.responseString;
        }
    }
}
