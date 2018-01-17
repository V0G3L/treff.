package org.pispeb.treff_server;

import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.sql.EntityManagerSQL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    private Server(Properties config) {
        // Start DB
        try {
            EntityManagerSQL.initialize(config);
        } catch (SQLException | NoSuchAlgorithmException e) {
            // TODO: error message
            e.printStackTrace();
        }
        AccountManager accountManager = EntityManagerSQL.getInstance();

        try {
            // Think of some clever way to indirect config string keys in a
            // centralized manner
            try (ServerSocket socket = new ServerSocket(
                    Integer.parseInt(config.getProperty("port")))) {
                // TODO: some way to exit (SIGTERM, shutdown command, ...)
                while (true) {
                    new ConnectionHandler(socket.accept(), accountManager)
                            .start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        //Stop DB

    }

}
