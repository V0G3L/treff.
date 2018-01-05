package org.pispeb.treff_client.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class to handle the Connection to the server
 * Created by vogel on 1/5/18.
 */

public class ConnectionHandler {
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    int request;

    public ConnectionHandler(String server, int port) {
        request = 0;
        try {
            socket = new Socket(server, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String request){
        //Send Request and give back the answer
        //or if no answer comes send Error after Timeout
        return null;
    }
}
