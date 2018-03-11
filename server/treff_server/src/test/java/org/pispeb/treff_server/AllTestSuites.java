package org.pispeb.treff_server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treff_server.commands.CommandTestSuite;
import org.pispeb.treff_server.commands.serializers.SerializerTestSuite;
import org.pispeb.treff_server.networking.NetworkingTestSuite;
import org.pispeb.treff_server.sql.SQLTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandTestSuite.class,
        NetworkingTestSuite.class,
        SerializerTestSuite.class,
        SQLTestSuite.class
})
public class AllTestSuites { }
