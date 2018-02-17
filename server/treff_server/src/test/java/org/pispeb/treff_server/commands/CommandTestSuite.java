package org.pispeb.treff_server.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GetPollDetailsCommandTest.class,
        GetUserIdCommandTest.class,
        LoginCommandTest.class,
        RegisterCommandTest.class,
        SendContactRequestCommandTest.class
})
public class CommandTestSuite { }
