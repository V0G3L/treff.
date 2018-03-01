package org.pispeb.treff_server.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author tim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AcceptContactRequestCommandTest.class,
        CancelContactRequestCommandTest.class,
        CreateGroupCommandTest.class,
        GetGroupDetailsCommandTest.class,
        GetPollDetailsCommandTest.class,
        GetUserDetailsCommandTest.class,
        GetUserIdCommandTest.class,
        LoginCommandTest.class,
        RegisterCommandTest.class,
        RejectContactRequestCommandTest.class,
        SendContactRequestCommandTest.class,
        UpdatePositionCommandTest.class
})
public class CommandTestSuite {
}
