package org.pispeb.treff_client.data.networking;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;

/**
 * Mock a ConnectionHandler for testing
 */

public class TestConnectionHandler {

    private int count = 10;
    private final ResponseListener responseListener;

    public TestConnectionHandler(String uri, ResponseListener
            responseListener)
            throws URISyntaxException, IOException, DeploymentException {
        this.responseListener = responseListener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        System.out.println(message);

        if (message.contains("\"cmd\":\"login\"")) {
            onMessage("{\"token\":\"aysdjhgafshd\"," +
                    "\"id\":162538}");
        } else if (message.contains("\"cmd\":\"list-groups\"")) {
            onMessage("{}");
        } else if (message.contains("\"cmd\":\"get-contact-list\"")) {
            onMessage("{\"contacts\":[1, 2, 3, 4]," +
                    "\"incoming-requests\":[5, 6]," +
                    "\"outgoing-requests\":[7]," +
                    "\"blocks\":[8]}");
        } else if (message.contains("\"cmd\":\"get-user-details\"")) {
            String name = "ERROR";
            int id = -1;
            if (message.contains("1")) {
                name = "Sample User"; id = 1;
            } else if (message.contains("2")) {
                name = "Peter"; id = 2;
            } else if (message.contains("3")) {
                name = "Jens"; id = 3;
            } else if (message.contains("4")) {
                name = "Tim"; id = 4;
            } else if (message.contains("5")) {
                name = "Simon"; id = 5;
            } else if (message.contains("6")) {
                name = "Matthias"; id = 6;
            } else if (message.contains("7")) {
                name = "Fabian"; id = 7;
            } else if (message.contains("8")) {
                name = "Lukas"; id = 8;
            }
            onMessage("{\"account\":{\"type\":\"account\"," +
                    "\"username\":\"" + name + "\"," +
                    "\"id\":" + id + "}}");
        } else if (message.contains("\"cmd\":\"create-group\"")) {
            onMessage("{\"id\":" + count++ + "}");
        } else if (message.contains("\"cmd\":\"get-group-details\"")) {
            onMessage("{}");
        } else if (message.contains("\"cmd\":\"send-chat-message\"")) {
            onMessage("{}");
        } else if (message.contains("\"cmd\":\"create-event\"")) {
            onMessage("{\"id\":" + count++ + "}");
        } else if (message.contains("\"cmd\":\"get-user-id\"")) {
            onMessage("{\"id\":" + count++ + "}");
        } else {
            onMessage("{}");
        }
    }

    @OnMessage
    public void onMessage(String message) {
        responseListener.onResponse(message);
    }

    public void disconnect() {
    }

public interface ResponseListener {
    void onResponse(String response);
}
}
