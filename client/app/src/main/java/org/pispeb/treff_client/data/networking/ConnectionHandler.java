package org.pispeb.treff_client.data.networking;

import android.os.HandlerThread;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.Session;

/**
 * Class to handle the Connection to the server
 * Created by vogel on 1/5/18.
 * TODO mock implementation (or even actual implementation)
 */
@ClientEndpoint
public class ConnectionHandler {

    private final Handler handler;
    private final URI uri;
    private final ResponseListener responseListener;
    private Session session;

    public ConnectionHandler(String uri, ResponseListener responseListener)
            throws URISyntaxException, IOException, DeploymentException {
        this.uri = new URI(uri);
        this.responseListener = responseListener;

        HandlerThread t = new HandlerThread("fgt",
                HandlerThread.NORM_PRIORITY);
        t.start();
        this.handler = new Handler(t.getLooper());
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        handler.post(() -> {
            if (session == null)
                session = connect(uri);

            Log.i("WebSocket Client", "Sending message " + message);
            boolean messageIsOut = false;
            while (!messageIsOut) {
                if (session.isOpen()) {
                    Log.i("CH", "socket is still open");
                    session.getAsyncRemote().sendText(message);
                    Log.i("CH", "message sent");
                    messageIsOut = true;
                } else {
                    Log.i("CH", "socket disconnected, will reconnect");
                }
            }
        });
    }

    @OnMessage
    public void onMessage(String message) {
        responseListener.onResponse(message);
    }

    public void disconnect() {
        if (session != null && session.isOpen())
            try {
                session.close();
            } catch (IOException e) { } // TODO: TODONT
    }

    private Session connect(URI uri) {
        Session session = null;
        // try connecting until it works
        while (session == null) {
            try {
                session = ContainerProvider.getWebSocketContainer()
                        .connectToServer(this, uri);
                Log.i("CH", "connected to socket");
            } catch (DeploymentException | IOException e) {
                Log.i("CH", "can't connect to socket, " +
                        "will retry in 5 seconds");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) { } // TODO: TODONT
            }

        }
        return session;
    }

    public interface ResponseListener {
        void onResponse(String response);
    }
}
