package org.pispeb.treff_server.commands;


import org.junit.Before;
import org.pispeb.treff_server.commands.CommandTest;
import org.pispeb.treff_server.commands.RegisterCommand;

import javax.json.JsonObject;

public abstract class LoginDependentTest extends CommandTest {

    protected String ownUsername = "w4rum";
    protected String ownPassword = "D4nz1g0rW4r";
    protected String token;
    protected int ownID;

    public LoginDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void register() {
        RegisteredUserData registeredUserData = registerAccount(
                ownUsername,ownPassword);
        token = registeredUserData.token;
        ownID = registeredUserData.id;

        inputBuilder.add("token", token);
    }

    protected RegisteredUserData registerAccount(String username,
                                                 String password) {

        String outputString = new RegisterCommand(accountManager).execute(
                String.format("{\"cmd\": \"register\"," +
                        "\"user\": \"%s\"," +
                        " \"pass\": \"%s\"}", username, password),
                mapper);
        JsonObject output = toJsonObject(outputString);
        return new RegisteredUserData(
                output.getString("token"),
                output.getInt("id"));
    }

    protected class RegisteredUserData {

        public final String token;
        public final int id;

        public RegisteredUserData(String token, int id) {
            this.token = token;
            this.id = id;
        }
    }
}

