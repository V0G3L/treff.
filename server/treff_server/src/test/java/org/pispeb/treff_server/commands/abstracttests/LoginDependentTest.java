package org.pispeb.treff_server.commands.abstracttests;


import org.junit.Before;
import org.pispeb.treff_server.commands.RegisterCommand;
import org.pispeb.treff_server.commands.abstracttests.CommandTest;

import javax.json.Json;
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
                ownUsername, ownPassword);
        token = registeredUserData.token;
        ownID = registeredUserData.id;

        inputBuilder.add("token", token);
    }

    protected RegisteredUserData registerAccount(String username,
                                                 String password) {

        RegisterCommand registerCommand
                = new RegisterCommand(accountManager, mapper);
        JsonObject output = runCommand(registerCommand,
                Json.createObjectBuilder()
                        .add("cmd", "register")
                        .add("user", username)
                        .add("pass", password));
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

