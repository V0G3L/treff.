package org.pispeb.treffpunkt.server.abstracttests;


import org.junit.Before;
import org.pispeb.treffpunkt.server.commands.RegisterCommand;

import javax.json.Json;
import javax.json.JsonObject;

public abstract class LoginDependentTest extends CommandTest {

    protected User ownUser;

    @Deprecated
    protected String ownUsername;
    @Deprecated
    protected String ownPassword;
    @Deprecated
    protected String token;
    @Deprecated
    protected int ownID;

    public LoginDependentTest(String cmd) {
        super(cmd);
    }

    @Before
    public void register() {
        ownUser = registerAccount("w4rum", "D4nz1g0rW4r");
        token = ownUser.token;
        ownID = ownUser.id;

        inputBuilder.add("token", ownUser.token);
    }

    protected User registerAccount(String username,
                                   String password) {

        RegisterCommand registerCommand
                = new RegisterCommand(sessionFactory, mapper);
        JsonObject output = runCommand(registerCommand,
                Json.createObjectBuilder()
                        .add("cmd", "register")
                        .add("user", username)
                        .add("pass", password));
        return new User(
                username,
                password,
                output.getInt("id"),
                output.getString("token"));

    }

    protected class User {

        public final String username;
        public final String password;
        public final int id;
        public final String token;

        public User(String username, String password, int id, String token) {
            this.username = username;
            this.password = password;
            this.id = id;
            this.token = token;
        }
    }
}

