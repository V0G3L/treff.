package org.pispeb.treffpunkt.server.abstracttests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.pispeb.treffpunkt.server.ConfigKeys;
import org.pispeb.treffpunkt.server.hibernate.AccountHib;
import org.pispeb.treffpunkt.server.hibernate.DataObjectHib;
import org.pispeb.treffpunkt.server.hibernate.UpdateHib;
import org.pispeb.treffpunkt.server.hibernate.UsergroupHib;

public class HibernateDependentTest extends DatabaseDependentTest {

    private static SessionFactory sessionFactory;
    protected static Session ss;
    protected static Transaction tx;

    @Before
    public void openSession() {
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
                .addAnnotatedClass(AccountHib.class)
                .addAnnotatedClass(DataObjectHib.class)
                .addAnnotatedClass(UpdateHib.class)
                .addAnnotatedClass(UsergroupHib.class)
                .buildSessionFactory();

        // open session and transaction
        ss = sessionFactory.openSession();
        tx = ss.beginTransaction();
    }

    @After
    public void closeSession() {
        tx.commit();
        ss.close();
    }
}
