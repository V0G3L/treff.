package org.pispeb.treff_server.commands;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * @author tim
 */
public class CreateGroupCommandTest extends MultipleUsersTest {

    public CreateGroupCommandTest() {
        super("create-group");
    }

    @Test
    public void execute() {
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownID)
                .add(users[1].id)
                .build();
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", "groupname")
                .add("members", members)
                .build();

        inputBuilder.add("group", group);
        JsonObject output = runCommand(createGroupCommand, inputBuilder);

        Assert.assertThat(output.getInt("id"),
                Matchers.greaterThanOrEqualTo(0));
    }
}