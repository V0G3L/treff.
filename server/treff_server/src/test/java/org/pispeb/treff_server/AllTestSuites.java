package org.pispeb.treff_server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.pispeb.treff_server.commands.CommandTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        CommandTestSuite.class
)
public class AllTestSuites { }
