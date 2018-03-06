package org.pispeb.treff_server.sql;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treff_server.networking.RequestHandlerTest;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SQLDatabaseTest.class
})
public class SQLTestSuite { }
