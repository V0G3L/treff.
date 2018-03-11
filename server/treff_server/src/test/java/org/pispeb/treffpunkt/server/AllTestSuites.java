package org.pispeb.treffpunkt.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treffpunkt.server.commands.CommandTestSuite;
import org.pispeb.treffpunkt.server.commands.serializers.SerializerTestSuite;
import org.pispeb.treffpunkt.server.networking.NetworkingTestSuite;
import org.pispeb.treffpunkt.server.sql.SQLTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandTestSuite.class,
        NetworkingTestSuite.class,
        SerializerTestSuite.class,
        SQLTestSuite.class
})
public class AllTestSuites { }
