package org.pispeb.treff_server.networking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PersistentConnectionTest.class,
        RequestHandlerTest.class
})
public class NetworkingTestSuite { }
