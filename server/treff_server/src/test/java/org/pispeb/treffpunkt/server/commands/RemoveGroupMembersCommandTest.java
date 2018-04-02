package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.*;
import java.util.HashSet;
import java.util.Set;

public class RemoveGroupMembersCommandTest extends GroupDependentTest {

    public RemoveGroupMembersCommandTest() {
        super("remove-group-members");
    }

    @Test
    public void valid() {
        // remove user 2 from group
        RemoveGroupMembersCommand removeGroupMembersCommand
                = new RemoveGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[2].id);
        inputBuilder.add("id", groupId)
                .add("members", members.build());

        JsonObject output = runCommand(removeGroupMembersCommand, inputBuilder);
        Assert.assertTrue(output.isEmpty());

        // check that user 2 was removed
        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(sessionFactory, mapper);
        JsonObjectBuilder input =
                getCommandStubForUser("get-group-details", users[0])
                        .add("id", groupId);
        JsonObject groupDesc = runCommand(getGroupDetailsCommand, input)
                .getJsonObject("group");
        Assert.assertEquals(groupDesc.getString("name"),
                groupName);
        JsonArray membersDesc = groupDesc.getJsonArray("members");

        // compare expected set of members (0 and 1) with actual member array
        Set<Integer> expectedMembers = new HashSet<>();
        expectedMembers.add(users[0].id);
        expectedMembers.add(users[1].id);

        Assert.assertEquals(2, membersDesc.size());
        Assert.assertTrue(expectedMembers.contains(membersDesc.getInt(0)));
        Assert.assertTrue(expectedMembers.contains(membersDesc.getInt(1)));

        // check that executing user didn't get an update
        assertNoUpdatesForUser(ownUser);

        // check that the other two users got an update
        for (int i  = 1; i <= 2; i++) {
            JsonObject update = getSingleUpdateForUser(users[i]);
            Assert.assertEquals(update.getString("type"),
                    UpdateType.USERGROUP_CHANGE.toString());

            // TODO check time-created
            Assert.assertEquals(update.getInt("creator"), ownUser.id);
            JsonObject updateGroupDesc = update.getJsonObject("usergroup");
            Assert.assertEquals(updateGroupDesc.getString("name"),
                    groupName);

            membersDesc = updateGroupDesc.getJsonArray("members");
            Assert.assertEquals(2, membersDesc.size());
            Assert.assertTrue(expectedMembers.contains(membersDesc.getInt(0)));
            Assert.assertTrue(expectedMembers.contains(membersDesc.getInt(1)));
        }
    }

    @Test
    public void invalidUserId() {
        int id = 5;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 23;
        RemoveGroupMembersCommand removeGroupMembersCommand
                = new RemoveGroupMembersCommand(sessionFactory, mapper);
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
                = new RemoveGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        inputBuilder.add("id", id)
                .add("members", members.build());

        JsonObject output = runCommand(removeGroupMembersCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Ignore
    @Test
    public void noPermission() {
        RemoveGroupMembersCommand removeGroupMembersCommand
                = new RemoveGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[1].id);
        JsonObjectBuilder input =
                getCommandStubForUser("add-group-members", users[2]);
        input.add("id", groupId)
                .add("members", members.build());

        JsonObject output = runCommand(removeGroupMembersCommand, input);

        Assert.assertEquals(output.getInt("error"), 2101);
    }

    @Test
    public void notInGroup() {
        RemoveGroupMembersCommand addGroupMembersCommand
                = new RemoveGroupMembersCommand(sessionFactory, mapper);
        JsonArrayBuilder members = Json.createArrayBuilder()
                .add(users[3].id);
        inputBuilder.add("id", groupId)
                .add("members", members.build());

        JsonObject output = runCommand(addGroupMembersCommand, inputBuilder);

        Assert.assertEquals(1511, output.getInt("error"));
    }
}