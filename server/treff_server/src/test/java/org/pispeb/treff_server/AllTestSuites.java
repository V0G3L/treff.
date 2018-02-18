package org.pispeb.treff_server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treff_server.commands.CommandTestSuite;
import org.pispeb.treff_server.commands.serializers.SerializerTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandTestSuite.class,
        SerializerTestSuite.class
})
public class AllTestSuites { }
