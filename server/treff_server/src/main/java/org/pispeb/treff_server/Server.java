package org.pispeb.treff_server;
import org.pispeb.treff_server.sql.EntityManagerSQL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

    static class ConnectionHandler extends Thread {
        private Socket socket;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                log("Error handling client");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }

    }
}
