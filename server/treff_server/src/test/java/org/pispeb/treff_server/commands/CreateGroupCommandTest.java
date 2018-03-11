package org.pispeb.treff_server.commands;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author tim
 */
public class CreateGroupCommandTest extends MultipleUsersTest {

    public CreateGroupCommandTest() {
        super("create-group");
    }

    @Test
    public void validRequest() {
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownUser.id)
                .add(users[1].id)
                .build();
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", "groupname")
                .add("members", members)
                .build();

        inputBuilder.add("group", group);
        JsonObject output = runCommand(createGroupCommand, inputBuilder);

        int groupId = output.getInt("id");
        Assert.assertThat(groupId,
                Matchers.greaterThanOrEqualTo(0));

        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(accountManager, mapper);
        JsonObjectBuilder input =
                getCommandStubForUser("get-group-details", users[0])
                .add("id", groupId);
        JsonObject groupDesc = runCommand(getGroupDetailsCommand, input)
                .getJsonObject("group");
        Assert.assertEquals(groupDesc.getString("name"),
                "groupname");
        JsonArray membersDesc = groupDesc.getJsonArray("members");
        Assert.assertTrue(membersDesc.getInt(0) == users[1].id
        || membersDesc.getInt(1) == users[1].id);
        Assert.assertTrue(membersDesc.getInt(0) == ownID
        || membersDesc.getInt(1) == ownID);

        assertNoUpdatesForUser(ownUser);

        JsonObject update = getSingleUpdateForUser(users[1]);
        Assert.assertEquals(update.getString("type"),
                UpdateType.USERGROUP_CHANGE.toString());

        // TODO check time-created
        Assert.assertEquals(update.getInt("creator"),ownID);
        JsonObject updateGroupDesc = update.getJsonObject("usergroup");
        Assert.assertEquals(updateGroupDesc.getString("name"),
                "groupname");
        JsonArray updateMembers = updateGroupDesc.getJsonArray("members");
        Assert.assertTrue(updateMembers.getInt(0) == ownID
                || updateMembers.getInt(0) == users[1].id);
        Assert.assertTrue(updateMembers.getInt(1) == ownID
                || updateMembers.getInt(1) == users[1].id);
    }

    @Test
    public void invalidUserId() {
        int id = 23;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 42;
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownUser.id)
                .add(id)
                .build();
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", "groupname")
                .add("members", members)
                .build();

        inputBuilder.add("group", group);

        JsonObject output = runCommand(createGroupCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }
}