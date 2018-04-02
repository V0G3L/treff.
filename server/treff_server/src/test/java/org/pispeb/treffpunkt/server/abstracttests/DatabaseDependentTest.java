package org.pispeb.treffpunkt.server.abstracttests;

import ch.vorburger.exec.ManagedProcessException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.pispeb.treffpunkt.server.ConfigKeys;
import org.pispeb.treffpunkt.server.TestDatabase;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.hibernate.DataObject;
import org.pispeb.treffpunkt.server.hibernate.Event;
import org.pispeb.treffpunkt.server.hibernate.GroupMembership;
import org.pispeb.treffpunkt.server.hibernate.Poll;
import org.pispeb.treffpunkt.server.hibernate.PollOption;
import org.pispeb.treffpunkt.server.hibernate.Update;
import org.pispeb.treffpunkt.server.hibernate.Usergroup;

import java.io.IOException;
import java.util.Properties;

public abstract class DatabaseDependentTest extends JsonDependentTest {

    protected static Properties config;
    protected SessionFactory sessionFactory;

    @BeforeClass
    public static void getDB() throws IOException, ManagedProcessException {
        config = TestDatabase.startDBAndGetConfig();
        DataObject.setProperties(config);
    }

    @Before
    public void initSession() {
        // init factory
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
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(GroupMembership.class)
                .addAnnotatedClass(Poll.class)
                .addAnnotatedClass(PollOption.class)
                .addAnnotatedClass(Update.class)
                .addAnnotatedClass(Usergroup.class)
                .buildSessionFactory();
    }

//    TODO: remove
//    @Before
//    public void prepareDB() throws SQLException, NoSuchAlgorithmException {
//        sqlDatabase = new SQLDatabase(config);
//        accountManager = sqlDatabase.getEntityManagerSQL();
//    }

    @After
    public void resetDB() throws ManagedProcessException {
        sessionFactory.close();
        TestDatabase.resetDB();
    }
}
