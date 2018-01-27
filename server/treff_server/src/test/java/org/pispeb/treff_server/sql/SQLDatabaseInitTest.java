package org.pispeb.treff_server.sql;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pispeb.treff_server.ConfigKeys;
import org.pispeb.treff_server.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

public class SQLDatabaseInitTest {

    private static final String CONFIG_DEFAULT_FILE_PATH
            = Server.class.getClassLoader().getResource("config_default" +
            ".properties").getFile();
    private static Properties config;
    private static DB db;

    @BeforeClass
    public static void startDB() throws ManagedProcessException, IOException {
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

        config.setProperty(ConfigKeys.DB_PORT.toString(), String.valueOf(PORT));
        config.setProperty(ConfigKeys.DB_ADDRESS.toString(), "localhost");
        config.setProperty(ConfigKeys.DB_USER.toString(), "root");
        config.setProperty(ConfigKeys.DB_PASS.toString(), "");
        config.setProperty(ConfigKeys.DB_DBNAME.toString(), "treffpunkttest");
    }

    @Test
    public void initWithoutException() throws SQLException, NoSuchAlgorithmException {
        SQLDatabase.initialize(config);
        Assert.assertNotNull(EntityManagerSQL.getInstance());
    }

    @AfterClass
    public static void stopDB() throws ManagedProcessException {
        db.stop();
    }
}
