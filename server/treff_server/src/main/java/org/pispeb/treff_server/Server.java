package org.pispeb.treff_server;

import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.RequestHandler;
import org.pispeb.treff_server.sql.SQLDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

public class Server {

    private static final String CONFIG_DEFAULT_FILE_PATH
            = Server.class.getClassLoader().getResource("config_default" +
            ".properties").getFile();
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static Server instance = null;

    private AccountManager accountManager;
    private RequestHandler requestHandler;

    public static Server getInstance() {
        if (instance == null)
            instance = new Server();

        return instance;
    }

    private Server() {
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

        SQLDatabase sqlDatabase = null;
        try {
            sqlDatabase = new SQLDatabase(config);
        } catch (SQLException | NoSuchAlgorithmException e) {
            // TODO: error message and exit
            e.printStackTrace();
        }

        accountManager = sqlDatabase.getEntityManagerSQL();
        requestHandler = new RequestHandler(accountManager);
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

}
