package org.pispeb.treff_server.networking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treff_server.commands.serializers.PollCompleteSerializerTest;
import org.pispeb.treff_server.commands.serializers
        .PollOptionCompleteSerializerTest;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestHandlerTest.class
})
public class NetworkingTestSuite { }
