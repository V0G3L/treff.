package org.pispeb.treff_server;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.sql.EntityManagerSQL;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Main class of the server.
 * Accepts network connections and creates {@link RequestHandler}s.
 *
 */
public class Server {
    private static int port;
    private static AccountManager accountManager;

    public static void main (String[] args) {
        new Server(Integer.valueOf(args[0]));
    }

    public Server (int port) {
        port = port;
        
        // Start DB
        accountManager = EntityManagerSQL.getInstance();

        try {
            ServerSocket socket = new ServerSocket(port);
            try {
                while (true) {
                    new ConnectionHandler(socket.accept(), accountManager).start();
                }
            } finally {
                socket.close();
            }
        }catch (IOException e) {
            e.printStackTrace(System.out);
        }

        //Stop DB

    }

}
