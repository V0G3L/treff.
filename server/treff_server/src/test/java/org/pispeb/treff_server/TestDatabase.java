package org.pispeb.treff_server;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public final class TestDatabase {

    private static final String CONFIG_DEFAULT_FILE_PATH
            = Server.class.getClassLoader().getResource("config_default" +
            ".properties").getFile();
    private static Properties config;
    private static DB db = null;

    public static Properties startDBAndGetConfig()
            throws ManagedProcessException, IOException {
        if (db == null) {
            System.out.println("Begin startup: " + new Date());
            // load default config
            Properties defaultConfig = new Properties();
            try (FileInputStream defaultConfigIn = new FileInputStream
                    (CONFIG_DEFAULT_FILE_PATH)) {
                defaultConfig.load(defaultConfigIn);
            }
            config = new Properties(defaultConfig);

            // start DB
            final int PORT = 13338;
            db = DB.newEmbeddedDB(PORT);
            db.start();
            db.run("CREATE DATABASE treffpunkttest;");

            config.setProperty(ConfigKeys.DB_PORT.toString(), String.valueOf
                    (PORT));

            config.setProperty(ConfigKeys.DB_ADDRESS.toString(), "localhost");
            config.setProperty(ConfigKeys.DB_USER.toString(), "root");
            config.setProperty(ConfigKeys.DB_PASS.toString(), "");
            config.setProperty(ConfigKeys.DB_DBNAME.toString(),
                    "treffpunkttest");
            System.out.println("End startup: " + new Date());
        }
        return config;
    }

    public static void resetDB() throws ManagedProcessException {
        // disable integrity checks, delete everything, turn checks back on
        db.run("SET FOREIGN_KEY_CHECKS = 0;");
        db.run("DROP DATABASE treffpunkttest;");
        db.run("CREATE DATABASE treffpunkttest;");
        db.run("SET FOREIGN_KEY_CHECKS = 1;");
    }

}
