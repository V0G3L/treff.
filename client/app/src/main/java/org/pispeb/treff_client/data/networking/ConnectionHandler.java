package org.pispeb.treff_client.data.networking;

import android.os.HandlerThread;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class to handle the Connection to the server
 * Created by vogel on 1/5/18.
 * TODO mock implementation (or even actual implementation)
 */

public class ConnectionHandler {

    public final String SERVER_IP; // Lukas Laptop
    private final int SERVER_PORT;
    private boolean mRun;

    private final Handler handler;

    // Sending to Server
    private PrintWriter mBufferOut;
    // Receiving answers from Server
    private BufferedReader mBufferIn;

    private OnMessageReceived mMessageListener;
    private Socket socket;

    public ConnectionHandler(String server, int port, OnMessageReceived
            listener) {
        this.SERVER_IP = server;
        this.SERVER_PORT = port;
        this.mMessageListener = listener;
        HandlerThread t = new HandlerThread("fgt",
                HandlerThread.NORM_PRIORITY);
        t.start();
        this.handler = new Handler(t.getLooper());
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mBufferIn = null;
        mBufferOut = null;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public synchronized void sendMessage(String message) {
        Log.i("TCP Client", "Sending message " + message);
        handler.post(() -> {
            try {
                if (socket.getInetAddress().isReachable(1000)) {
                    Log.i("CH", "socket connected");
                    if (mBufferOut != null && !mBufferOut.checkError()) {
                        mBufferOut.println(message);
                        mBufferOut.flush();
                    }
                } else {
                    Log.i("CH", "socket disconnected");
                }
            } catch (IOException e) {
                mMessageListener.restartConnection();
            }
        });
    }

    /**
     * TODO: doc
     */
    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVER_PORT);

            try {

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by
                // the server
                while (mRun) {

                    String mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect
                // to this socket after it is closed, which means a new
                // socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }
    }

    public boolean isRunning() {
        return mRun;
    }

    /**
     * Declare the interface. The method messageReceived(String message)
     * must be implemented in the MyActivity
     * class at on asyncTask doInBackground
     */
    public interface OnMessageReceived {
        void messageReceived(String message);
        void restartConnection();
    }
}
