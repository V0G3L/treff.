package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.RemoveGroupMembersCommand;
import org.pispeb.treff_server.commands.GetGroupDetailsCommand;
import org.pispeb.treff_server.commands.RemoveGroupMembersCommand;
import org.pispeb.treff_server.commands.abstracttests.GroupDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.*;

import static org.junit.Assert.*;

public class RemoveGroupMembersCommandTest extends GroupDependentTest {

    public RemoveGroupMembersCommandTest() {
        super("remove-group-members");
    }

    @Test
    public void valid() {
        RemoveGroupMembersCommand removeGroupMembersCommand
                = new RemoveGroupMembersCommand(accountManager, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[2].id);
        inputBuilder.add("id", groupId)
                .add("members", members.build());

        runCommand(removeGroupMembersCommand, inputBuilder);

        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(accountManager, mapper);
        JsonObjectBuilder input =
                getCommandStubForUser("get-group-details", users[0])
                        .add("id", groupId);
        JsonObject groupDesc = runCommand(getGroupDetailsCommand, input)
                .getJsonObject("group");
        Assert.assertEquals(groupDesc.getString("name"),
                "doomedtodie");
        JsonArray membersDesc = groupDesc.getJsonArray("members");
        Assert.assertTrue(membersDesc.getInt(0) == users[1].id
        || membersDesc.getInt(1) == users[1].id);
        Assert.assertTrue(membersDesc.getInt(0) == ownID
        || membersDesc.getInt(1) == ownID);
        Assert.assertEquals(2, membersDesc.size());

        Assert.assertEquals(0, getUpdatesForUser(ownUser).size());

        JsonObject update = getSingleUpdateForUser(users[1]);
        Assert.assertEquals(update.getString("type"),
                UpdateType.USERGROUP_CHANGE.toString());

        update = getSingleUpdateForUser(users[1]);
        Assert.assertEquals(update.getString("type"),
                UpdateType.USERGROUP_CHANGE.toString());

        // TODO check time-created
        Assert.assertEquals(update.getInt("creator"), ownUser.id);
        JsonObject updateGroupDesc = update.getJsonObject("usergroup");
        Assert.assertEquals(updateGroupDesc.getString("name"),
                "groupname");
    }

    @Test
    public void invalidUserId() {
        int id = 5;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 23;
        RemoveGroupMembersCommand removeGroupMembersCommand
                = new RemoveGroupMembersCommand(accountManager, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(id);
        inputBuilder.add("id", groupId)
                .add("members", members.build());

        JsonObject output = runCommand(removeGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }

    @Test
    public void invalidGroupId() {
        int id = 23;
        while (id == groupId)
            id += 5;
        RemoveGroupMembersCommand removeGroupMembersCommand
                = new RemoveGroupMembersCommand(accountManager, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        inputBuilder.add("id", id)
                .add("members", members.build());

        JsonObject output = runCommand(removeGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Test
    public void noPermission() {
        //TODO
    }

    @Test
    public void notInGroup() {
        RemoveGroupMembersCommand addGroupMembersCommand
                = new RemoveGroupMembersCommand(accountManager, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        inputBuilder.add("id", groupId)
                .add("members", members.build());

        JsonObject output = runCommand(addGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1511);
    }
}