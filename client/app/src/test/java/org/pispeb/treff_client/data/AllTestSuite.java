package org.pispeb.treff_client.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treff_client.data.networking.NetworkingTestSuite;
import org.pispeb.treff_client.data.repositories.RepositoryTestSuite;

/**
 * All tests that don't require to be run on the device
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NetworkingTestSuite.class,
        RepositoryTestSuite.class
})
public class AllTestSuite {
}
