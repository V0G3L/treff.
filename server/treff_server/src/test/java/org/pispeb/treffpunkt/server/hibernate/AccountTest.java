package org.pispeb.treffpunkt.server.hibernate;

import org.junit.Before;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.DatabaseDependentTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AccountTest extends DatabaseDependentTest {

    private static Account instance;

    private final String username = "w4rum";
    private final String email = "foo@bar.info";
    private final String password = "4+1d10+2AP";
    private int id;


    @Before
    public void createAccount() {
        instance = new Account();
        instance.setUsername(username);
        instance.setEmail(email);
        instance.setPassword(password);
        id = (int) ss.save(instance);
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
    public void delete() {
        assertNotNull(instance);

        ss.delete(instance);
        ss.flush();

        assertNull(ss.get(Account.class, id));
    }

    private void saveAndReload() {
        ss.update(instance);
        reload();
    }

    private void reload() {
        tx.commit();
        tx = ss.beginTransaction();
        instance = ss.get(Account.class, id);
    }

}