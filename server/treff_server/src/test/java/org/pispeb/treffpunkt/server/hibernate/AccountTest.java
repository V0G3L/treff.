package org.pispeb.treffpunkt.server.hibernate;

import org.junit.Before;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.DatabaseDependentTest;
import org.pispeb.treffpunkt.server.abstracttests.HibernateTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AccountTest extends HibernateTest {

    private static Account instance;
    private static Account instance2;

    private final String username = "w4rum";
    private final String email = "foo@bar.info";
    private final String password = "4+1d10+2AP";
    private int id;
    private final String username2 = "w4rum2";
    private final String email2 = "foo2222@bar.info";
    private final String password2 = "4+1d10+2AP222";
    private int id2;


    @Before
    public void createAccounts() {
        instance = new Account();
        instance.setUsername(username);
        instance.setEmail(email);
        instance.setPassword(password);
        instance2 = new Account();
        instance2.setUsername(username2);
        instance2.setEmail(email2);
        instance2.setPassword(password2);
        id = (int) ss.save(instance);
        id2 = (int) ss.save(instance2);
        reload();
    }

    @Test
    public void noChange() {
        assertEquals(instance.getUsername(), username);
        assertEquals(instance.getEmail(), email);
        assertTrue(instance.checkPassword(password));
    }

    @Test
    public void change() {
        String newUsername = "foo";
        String newPass = "bar";
        String newEmail = "foo2@bar.info";
        instance.setUsername(newUsername);
        instance.setPassword(newPass);
        instance.setEmail(newEmail);
        saveAndReload();

        assertEquals(instance.getUsername(), newUsername);
        assertEquals(instance.getEmail(), newEmail);
        assertTrue(instance.checkPassword(newPass));
        assertFalse(instance.checkPassword(password));
    }

    @Test
    public void contactSyncWithCommit() {
        instance.sendContactRequest(instance2);
        assertTrue(instance.getAllOutgoingContactRequests().containsKey(instance2.getID()));
        saveAndReload();
        assertTrue(instance2.getAllIncomingContactRequests().containsKey(instance.getID()));
    }

    @Test
    public void contactSyncWithoutCommit() {
        instance.sendContactRequest(instance2);
        assertTrue(instance.getAllOutgoingContactRequests().containsKey(instance2.getID()));
        assertTrue(instance2.getAllIncomingContactRequests().containsKey(instance.getID()));
    }

    @Test
    public void delete() {
        assertNotNull(instance);

        ss.delete(instance);
        ss.flush();

        assertNull(ss.get(Account.class, id));
    }

    private void saveAndReload() {
        ss.update(instance);
        ss.update(instance2);
        reload();
    }

    private void reload() {
        tx.commit();
        tx = ss.beginTransaction();
        instance = ss.get(Account.class, id);
    }

}