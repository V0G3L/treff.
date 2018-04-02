package org.pispeb.treffpunkt.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.AccountManager;
import org.pispeb.treffpunkt.server.hibernate.DataObject;
import org.pispeb.treffpunkt.server.hibernate.Update;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;
import org.pispeb.treffpunkt.server.networking.RequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Server {

    private static final String CONFIG_DEFAULT_FILE_PATH
            = Server.class.getClassLoader().getResource("config_default" +
            ".properties").getFile();
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static Server instance = null;
    private final SessionFactory sessionFactory;
    private final RequestHandler requestHandler;

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

        // TODO: init Hibernate
        sessionFactory = new Configuration()
                .configure()
                .setProperty("hibernate.connection.url",
                        String.format("jdbc:mysql://%s:%d/%s?serverTimezone=UTC",
                                config.getProperty(ConfigKeys.DB_ADDRESS.toString()),
                                Integer.parseInt(config.getProperty(ConfigKeys.DB_PORT.toString())),
                                config.getProperty(ConfigKeys.DB_DBNAME.toString())))
                .setProperty("hibernate.connection.username",
                        config.getProperty(ConfigKeys.DB_USER.toString()))
                .setProperty("hibernate.connection.password",
                        config.getProperty(ConfigKeys.DB_PASS.toString()))
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(DataObject.class)
                .addAnnotatedClass(Update.class)
                .addAnnotatedClass(Usergroup.class)
                .buildSessionFactory();
//            accountManager = new SQLDatabase(config).getEntityManagerSQL();
            requestHandler = new RequestHandler(sessionFactory);
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

}
