package org.pispeb.treff_server;
import org.pispeb.treff_server.sql.EntityManagerSQL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Main class of the server handles network connection and gives requests to the database
 */
public class Server {
    private static int port;
    private static AccountManager aM;

    public static void main (String[] args) {
        new Server(Integer.valueOf(args[0]));
    }

    public Server (int port) {
        port = port;
        
        //Start DB
        aM = EntityManagerSQL.getInstance();

        try {
            ServerSocket socket = new ServerSocket(port);
            try {
                while (true) {
                    new ConnectionHandler(socket.accept()).start();
                }
            } finally {
                socket.close();
            }
        }catch (IOException e) {
            e.printStackTrace(System.out);
        }

        //Stop DB

    }

    /**
     * Class for handling single Connections
     */
    static class ConnectionHandler extends Thread {
        private Socket socket;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Method that handles a Connection
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
}
