package org.pispeb.treffpunkt.client.data.networking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treffpunkt.client.data.networking.commands.CommandsTestSuite;

/**
 * All networking related tests
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandsTestSuite.class,
        BasicRequestEncoderTest.class,
        RequestEncoderCommandsTest.class
})
public class NetworkingTestSuite {
}
