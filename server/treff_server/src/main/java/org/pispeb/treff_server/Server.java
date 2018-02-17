package org.pispeb.treff_server;

import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.ConnectionHandler;
import org.pispeb.treff_server.sql.EntityManagerSQL;
import org.pispeb.treff_server.sql.SQLDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Main class of the server.
 * Accepts network connections and creates {@link ConnectionHandler}s.
 */
public class Server {

    private static final String CONFIG_DEFAULT_FILE_PATH
            = Server.class.getClassLoader().getResource("config_default" +
            ".properties").getFile();
    private static final String CONFIG_FILE_PATH = "config.properties";

    public static void main(String[] args) {
        // load default config
        Properties defaultConfig = new Properties();
        try (FileInputStream defaultConfigIn = new FileInputStream
                (CONFIG_DEFAULT_FILE_PATH)) {
            defaultConfig.load(defaultConfigIn);
        } catch (IOException e) {
            // TODO: error message
            e.printStackTrace();
        }

        // load actual config, using default config as fallback
        Properties config = new Properties(defaultConfig);
        if (new File(CONFIG_FILE_PATH).exists()) {
            try (FileInputStream configIn = new FileInputStream
                    (CONFIG_DEFAULT_FILE_PATH)) {
                config.load(configIn);
            } catch (IOException e) {
                // TODO: error message
                e.printStackTrace();
            }
        }

        new Server(config);
    }

    public Server(Properties config) {
        // Start DB

        SQLDatabase sqlDatabase = null;
        try {
            sqlDatabase = new SQLDatabase(config);
        } catch (SQLException | NoSuchAlgorithmException e) {
            // TODO: error message and exit
            e.printStackTrace();
        }

        AccountManager accountManager = sqlDatabase.getEntityManagerSQL();

        try (ServerSocket socket = new ServerSocket(
                    Integer.parseInt(config.getProperty("port")))) {
            // TODO: remove debug message
            System.out.println("Listening on port "
                    + socket.getLocalSocketAddress().toString());
            //noinspection UnusedAssignment WTF IntelliJ?
            DatabaseExceptionHandler exceptionHandler
                    = new DatabaseExceptionHandler();
            // Think of some clever way to indirect config string keys in a
            // centralized manner
            // TODO: some way to exit (SIGTERM, shutdown command, ...)
            while (!exceptionHandler.hasExceptionHappened()) {
                new ConnectionHandler(socket.accept(), accountManager,
                        exceptionHandler)
                        .start();
            }
            System.err.println(exceptionHandler.getCaughtException().getMessage());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        //Stop DB

    }

}
