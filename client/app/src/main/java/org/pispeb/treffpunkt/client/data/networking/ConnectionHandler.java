package org.pispeb.treffpunkt.client.data.networking;

import android.os.HandlerThread;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
@Deprecated
public class ConnectionHandler {

    private final static int TIMEOUT_MS = 15000;
    private final static int WAIT_ON_CANT_CONNECT_MS = 5000;

    private final Handler handler;
    private final URI uri;
    private final ResponseListener responseListener;
    private Session session;

    private final Handler timeoutHandler = new Handler();
    private final Lock timeoutLock = new ReentrantLock();
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
    public void sendMessage(String message, boolean abortOnCantConnect) {
        handler.post(() -> {
            // if not connected, connect
            if (session == null)
                session = connect(uri);

            // if connecting failed, i.e. connect returned null,
            // abort
            if ((session == null) && abortOnCantConnect) {
                Log.i("CH", "can't connect, aborting send");
                responseListener.onCantConnect();
                return;
            }

            Log.i("CH", "Sending message " + message);
            boolean messageIsOut = false;
            while (!messageIsOut) {
                // if connection couldn't be established in the first place
                // wait a few seconds, then try to connect again
                if (session == null) {
                    Log.i("CH", "can't connect to socket, will retry in 5 " +
                            "seconds");
                    try {
                        Thread.sleep(WAIT_ON_CANT_CONNECT_MS);
                    } catch (InterruptedException ignored) {
                    }
                    session = connect(uri);
                }
                // if connection was established before but just lost,
                // try again immediately
                else if (!session.isOpen()) {
                    session = connect(uri);
                    Log.i("CH", "socket disconnected, will try reconnecting " +
                            "immediately");
                }
                // if connection was established and is still open, send message
                else {
                    Log.i("CH", "socket is still open");
                    session.getAsyncRemote().sendText(message);
                    Log.i("CH", "message sent");
                    messageIsOut = true;
                    startTimeoutTimer();
                    Log.i("CH", String.format("timeout scheduled in %s " +
                            "seconds, leaving sendMessage", TIMEOUT_MS / 1000));
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

    private boolean cancelTimeoutTimer() {
        try {
            timeoutLock.lock();
            // if the message arrived too late and the timeout routine has
            // already run, signal that cancelling was unsuccessful
            if (hitTimeout)
                return false;

            Log.i("CH", "stop timeout came through");

            // de-schedule timeout job
            timeoutHandler.removeCallbacksAndMessages(timeoutThreadToken);
            Log.i("CH", "de-scheduled timeout thread");

            // if timeout job was already started, signal it to abort
            if (timeoutThread.isAlive()) {
                Log.i("CH", "interrupting timeout job");
                timeoutThread.interrupt();
            }
        } finally {
            timeoutLock.unlock();
        }
        return true;
    }

    @OnMessage
    public void onMessage(String message) {
        Log.i("CH", "onMessage started");
        if (cancelTimeoutTimer()) {
            Log.i("CH", "Message received: " + message);
            responseListener.onResponse(message);
        }
    }

    public void disconnect() {
        if (session != null && session.isOpen())
            try {
                cancelTimeoutTimer();
                session.close();
            } catch (IOException ignored) { } // TODO: TODONT
    }

    /**
     * Tries to connect to the specified URI.
     *
     * @param uri The URI to connect to.
     * @return The newly opened {@code Session} if connecting was successful or
     * {@code null} if the connection couldn't be established.
     */
    private Session connect(URI uri) {
        Log.i("CH", String.format("Connecting to %s", uri));
        try {
            Session session = ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, uri);
            Log.i("CH", "Connected to socket");
            return session;
        } catch (DeploymentException | IOException e) {
            // if connecting fails, return null
            return null;
        }
    }

    interface ResponseListener {
        void onResponse(String response);

        void onTimeout();

        /**
         * Called when the {@code ConnectionHandler} can't connect to the
         * server and aborts message sending.
         */
        void onCantConnect();
    }
}
