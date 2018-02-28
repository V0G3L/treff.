package org.pispeb.treff_server.commands;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.MultipleUsersTest;
import org.pispeb.treff_server.commands.descriptions.UsergroupCreateDescription;
import org.pispeb.treff_server.commands.updates.UpdateType;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.interfaces.Usergroup;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashSet;
import java.util.Set;

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

        int groupId = output.getInt("id");
        Assert.assertThat(groupId,
                Matchers.greaterThanOrEqualTo(0));
        Usergroup Usergroup = accountManager.getAccount(ownID).getAllGroups().get(groupId);
        Assert.assertEquals(Usergroup.getName(),"groupname");
        HashSet<Integer> memberIds = new HashSet<Integer>();
        memberIds.add(ownID);
        memberIds.add(users[1].id);
        Assert.assertTrue(Usergroup.getAllMembers().keySet().containsAll(memberIds)
                && memberIds.containsAll(Usergroup.getAllMembers().keySet()));

        // TODO updates for some reason aren't in the database
        JsonObject update = getSingleUpdateForUser(users[1].id);
        Assert.assertEquals(update.getString("type"), UpdateType.USERGROUP_CHANGE.toString());
        // TODO check time-created
        Assert.assertEquals(update.getInt("creator"),ownID);
        JsonObject groupDesc = update.getJsonObject("usergroup");
        Assert.assertEquals(groupDesc.getString("name"), "groupname");
        JsonArray updateMembers = groupDesc.getJsonArray("members");
        Assert.assertTrue(updateMembers.getInt(0) == ownID
                || updateMembers.getInt(0) == users[1].id);
        Assert.assertTrue(updateMembers.getInt(1) == ownID
                || updateMembers.getInt(1) == users[1].id);
    }


    // TODO implement Syntax-checks
    @Test
    public void invalidSyntaxA() {
         CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        JsonObject group = Json.createObjectBuilder()
                .add("All your base", "are belong to us")
                .build();

        inputBuilder.add("group", group);
        JsonObject output = runCommand(createGroupCommand, inputBuilder);

        Assert.assertEquals(output.getInt("Error"), 1000);
    }

    @Test
    public void invalidSyntaxB() {
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        inputBuilder.add("All your base", "are belong to us");
        JsonObject output = runCommand(createGroupCommand, inputBuilder);

        Assert.assertEquals(output.getInt("Error"), 1000);
    }


    @Test
    public void invalidToken() {
        JsonObjectBuilder inputBuilder = Json.createObjectBuilder();
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        inputBuilder.add("cmd", "create-group")
                .add("token", "0");

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

        Assert.assertEquals(output.getInt("error"), 1100);
    }

    @Test
    public void invalidUserId() {
        int id = 23;
        while (id == ownID || id == users[1].id || id == users[2].id || id == users[3].id)
            id += 42;
        CreateGroupCommand createGroupCommand
                = new CreateGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownID)
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