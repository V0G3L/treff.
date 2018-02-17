package org.pispeb.treff_server.commands;

import org.junit.Before;

/**
 * @author tim
 */
public abstract class MultipleUsersTest extends LoginDependentTest {

    protected String[] userNames = {
            ownUsername,
            "didyoueverhear",
            "Comet sighted",
            "Ly'leth Lunastre"
    };
    protected String[] userPasswords = {
            ownPassword,
            "thetragedyofdarthplagueisthewise",
            "If only we had comet sense...",
            "Your arrival is fortuitous."
    };
    protected RegisteredUserData[] users = new RegisteredUserData[4];

    public MultipleUsersTest(String cmd) {
        super(cmd);
    }

    @Before
    public void registerOtherUsers() {
        users[0] = new RegisteredUserData(token, ownID);
        for (int i = 1; i < 4; i++)
            users[i] = registerAccount(userNames[i], userPasswords[i]);
    }
}
