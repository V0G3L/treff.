package org.pispeb.treff_server;

import org.pispeb.treff_server.interfaces.AccountManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class for handling single connections
 */
class ConnectionHandler extends Thread {
    private final Socket socket;
    private final AccountManager accountManager;

    public ConnectionHandler(Socket socket, AccountManager accountManager) {
        this.socket = socket;
        this.accountManager = accountManager;
    }

    /**
     * Method that handles a connection
     */
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
