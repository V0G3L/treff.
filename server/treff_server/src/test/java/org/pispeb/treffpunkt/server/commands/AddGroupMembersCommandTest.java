package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class AddGroupMembersCommandTest extends GroupDependentTest {

    public AddGroupMembersCommandTest() {
        super("add-group-members");
    }

    @Test
    public void valid() {
        AddGroupMembersCommand addGroupMembersCommand
                = new AddGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        inputBuilder.add("id",groupId)
                .add("members", members.build());

        runCommand(addGroupMembersCommand, inputBuilder);

        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(sessionFactory, mapper);
        JsonObjectBuilder input =
                getCommandStubForUser("get-group-details", users[0])
                .add("id", groupId);
        JsonObject groupDesc = runCommand(getGroupDetailsCommand, input)
                .getJsonObject("group");
        Assert.assertEquals(groupDesc.getString("name"),
                "<script>confirm('press cancel to wipe all data')</script>");

        assertNoUpdatesForUser(ownUser);

        JsonObject update = getSingleUpdateForUser(users[1]);
        Assert.assertEquals(update.getString("type"),
                UpdateType.USERGROUP_CHANGE.toString());

        // TODO check time-created
        Assert.assertEquals(update.getInt("creator"), ownUser.id);
        JsonObject updateGroupDesc = update.getJsonObject("usergroup");
        Assert.assertEquals(updateGroupDesc.getString("name"),
                "<script>confirm('press cancel to wipe all data')</script>");
    }

    @Test
    public void invalidUserId() {
        int id = 5;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 23;
        AddGroupMembersCommand addGroupMembersCommand
                = new AddGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(id);
        inputBuilder.add("id",groupId)
                .add("members", members.build());

        JsonObject output = runCommand(addGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }

    @Test
    public void invalidGroupId() {
        int id = 23;
        while (id == groupId)
            id += 5;
        AddGroupMembersCommand addGroupMembersCommand
                = new AddGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        inputBuilder.add("id", id)
                .add("members", members.build());

        JsonObject output = runCommand(addGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Ignore
    @Test
    public void noPermission() {
        AddGroupMembersCommand addGroupMembersCommand
                = new AddGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        JsonObjectBuilder input =
                getCommandStubForUser("add-group-members", users[2]);
        input.add("id", groupId)
                .add("members", members.build());

        JsonObject output = runCommand(addGroupMembersCommand, input);

        Assert.assertEquals(output.getInt("error"), 2101);
    }

    @Test
    public void alreadyInGroup() {
        AddGroupMembersCommand addGroupMembersCommand
                = new AddGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[2].id);
        inputBuilder.add("id", groupId)
                .add("members", members.build());
        JsonObject output = runCommand(addGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1510);
    }

}