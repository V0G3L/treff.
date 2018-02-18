package org.pispeb.treff_server.sql;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.DatabaseDependentTest;

public class SQLDatabaseInitTest extends DatabaseDependentTest {

    @Test
    public void initWithoutException() {
        Assert.assertNotNull(accountManager);
    }

}
