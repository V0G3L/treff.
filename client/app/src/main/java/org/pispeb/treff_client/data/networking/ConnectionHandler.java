package org.pispeb.treff_client.data.networking;

import android.os.HandlerThread;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.Session;

/**
 * Class to handle the Connection to the server
 */
@ClientEndpoint
public class ConnectionHandler {

    private final static int TIMEOUT_MS = 15000;

    private final Handler handler;
    private final URI uri;
    private final ResponseListener responseListener;
    private Session session;

    private final Handler timeoutHandler = new Handler();
    private final Lock timeoutLock= new ReentrantLock();
    private Thread timeoutThread;
    private Object timeoutThreadToken = new Object();
    private boolean hitTimeout;

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
                    startTimeoutTimer();
                    Log.i("CH", String.format("timeout scheduled in %s " +
                            "seconds, leaving sendMessage", TIMEOUT_MS / 1000));
                } else {
                    Log.i("CH", "socket disconnected, will reconnect");
                    session = connect(uri);
                }
            }
        });
    }

    private void startTimeoutTimer() {
        hitTimeout = false;
        timeoutThread = new Thread(() -> {
            try {
                Log.i("CH", "timeout job started");
                timeoutLock.lockInterruptibly();
                try {
                    // signal onMessage call that might arrive just in time to
                    // discard its message
                    hitTimeout = true;
                    Log.i("CH", "timeout job came through");

                    disconnect();
                } finally {
                    timeoutLock.unlock();
                }
                responseListener.onTimeout();
            } catch (InterruptedException e) {
                // if the onMessage call arrived just in time, acquired the
                // lock first and now interrupted this timeout job, abort
                // gracefully
                Log.i("CH", "timeout job interrupted");
            }
        });
        timeoutHandler.postAtTime(timeoutThread::start, timeoutThreadToken,
                SystemClock.uptimeMillis() + TIMEOUT_MS);
    }

    @OnMessage
    public void onMessage(String message) {
        Log.i("CH", "onMessage started");
        try {
            timeoutLock.lock();
            // if the message arrived too late and the timeout routine has
            // already run, discard message
            if (hitTimeout)
                return;

            Log.i("CH", "onMessage came through");

            // de-schedule timeout job
            timeoutHandler.removeCallbacksAndMessages(timeoutThreadToken);
            Log.i("CH", "onMessage de-scheduled timeout thread");

            // if timeout job was already started, signal it to abort
            if (timeoutThread.isAlive()) {
                Log.i("CH", "interrupting timeout job");
                timeoutThread.interrupt();
            }


            responseListener.onResponse(message);
        } finally {
            timeoutLock.unlock();
        }
    }

    public void disconnect() {
        if (session != null && session.isOpen())
            try {
                session.close();
            } catch (IOException e) {
            } // TODO: TODONT
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
                } catch (InterruptedException e1) {
                } // TODO: TODONT
            }

        }
        return session;
    }

    public interface ResponseListener {
        void onResponse(String response);

        void onTimeout();
    }
}
