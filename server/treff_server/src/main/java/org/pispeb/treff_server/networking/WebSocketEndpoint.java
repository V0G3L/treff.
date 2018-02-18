package org.pispeb.treff_server.networking;

import org.pispeb.treff_server.Server;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author tim
 */
@ServerEndpoint("/ws")
public class WebSocketEndpoint {

    @OnMessage
    public String onMessage(String message) {
        Server server = Server.getInstance();
        Response response = server.getRequestHandler().handleRequest(message);

        if (response.requestedPersistentConnection) {
            throw new UnsupportedOperationException(); // TODO: implement
        } else {
            return response.responseString;
        }
    }
}
