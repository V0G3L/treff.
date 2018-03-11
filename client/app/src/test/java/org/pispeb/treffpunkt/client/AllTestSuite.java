package org.pispeb.treffpunkt.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treffpunkt.client.data.entities.EntitiesTestSuite;
import org.pispeb.treffpunkt.client.data.networking.NetworkingTestSuite;
import org.pispeb.treffpunkt.client.data.repositories.RepositoryTestSuite;
import org.pispeb.treffpunkt.client.view.viewmodels.ViewModelsTestSuite;

/**
 * All tests that don't require to be run on the device
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NetworkingTestSuite.class,
        EntitiesTestSuite.class,
        RepositoryTestSuite.class,
        ViewModelsTestSuite.class
})
public class AllTestSuite {
}
