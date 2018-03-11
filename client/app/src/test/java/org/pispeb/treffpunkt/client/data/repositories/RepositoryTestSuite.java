package org.pispeb.treffpunkt.client.data.repositories;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Lukas on 3/6/2018.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ChatRepositoryTest.class,
        EventRepositoryTest.class,
        UserRepositoryTest.class,
        UserGroupRepositoryTest.class
})
public class RepositoryTestSuite {
}
