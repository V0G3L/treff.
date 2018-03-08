package org.pispeb.treff_server.sql;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.DatabaseDependentTest;

/**
 * @author tim
 */
public class SQLDatabaseTest extends DatabaseDependentTest {

    @Test
    public void initWithoutException() {
        Assert.assertNotNull(accountManager);
    }

}
