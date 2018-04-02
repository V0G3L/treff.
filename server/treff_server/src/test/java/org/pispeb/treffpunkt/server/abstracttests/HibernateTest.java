package org.pispeb.treffpunkt.server.abstracttests;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;

public class HibernateTest extends DatabaseDependentTest {

    protected static Session ss;
    protected static Transaction tx;

    @Before
    public void openSession() {
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
