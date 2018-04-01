package org.pispeb.treffpunkt.server.abstracttests;

import ch.vorburger.exec.ManagedProcessException;
import org.junit.After;
import org.junit.BeforeClass;
import org.pispeb.treffpunkt.server.TestDatabase;
import org.pispeb.treffpunkt.server.interfaces.AccountManager;

import java.io.IOException;
import java.util.Properties;

public abstract class DatabaseDependentTest extends JsonDependentTest {

    protected static Properties config;
    protected AccountManager accountManager;

    @BeforeClass
    public static void getDB() throws IOException, ManagedProcessException {
        config = TestDatabase.startDBAndGetConfig();
    }

//    TODO: remove
//    @Before
//    public void prepareDB() throws SQLException, NoSuchAlgorithmException {
//        sqlDatabase = new SQLDatabase(config);
//        accountManager = sqlDatabase.getEntityManagerSQL();
//    }

    @After
    public void resetDB() throws ManagedProcessException {
        TestDatabase.resetDB();
    }
}
