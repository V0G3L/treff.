package org.pispeb.treff_server;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.update_notifier.PersistentConnection;

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
    private final DatabaseExceptionHandler exceptionHandler;

    public ConnectionHandler(Socket socket, AccountManager accountManager,
                             DatabaseExceptionHandler exceptionHandler) {
        this.socket = socket;
        this.accountManager = accountManager;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Method that handles a connection
     */
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // get requeststring
            String request = null;
            RequestHandler requestHandler
                    = new RequestHandler(request, accountManager);
            RequestHandlerResponse response = requestHandler.run();
            if (!response.requestedPersistentConnection) {
                // send to client
            } else {
                new PersistentConnection(out, accountManager,
                        response.accountID);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DatabaseException e) {
            exceptionHandler.notifyOfException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
