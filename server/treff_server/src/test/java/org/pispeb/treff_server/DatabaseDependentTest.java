package org.pispeb.treff_server;

import ch.vorburger.exec.ManagedProcessException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.sql.SQLDatabase;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DatabaseDependentTest extends JsonDependentTest {

    protected static Properties config;
    protected SQLDatabase sqlDatabase;
    protected AccountManager accountManager;

    @BeforeClass
    public static void getDB() throws IOException, ManagedProcessException {
        config = TestDatabase.startDBAndGetConfig();
    }

    @Before
    public void prepareDB() throws SQLException, NoSuchAlgorithmException {
        sqlDatabase = new SQLDatabase(config);
        accountManager = sqlDatabase.getEntityManagerSQL();
    }

    @After
    public void resetDB() throws ManagedProcessException {
        TestDatabase.resetDB();
    }
}
