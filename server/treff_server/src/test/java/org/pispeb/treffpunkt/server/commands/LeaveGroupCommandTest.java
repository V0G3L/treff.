package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.*;
import java.util.HashSet;
import java.util.Set;

public class LeaveGroupCommandTest extends GroupDependentTest {

    public LeaveGroupCommandTest() {
        super("leave-group");
    }

    @Test
    public void valid() {
        // leave group
        JsonObject output = execute(groupId);
        Assert.assertTrue(output.isEmpty());

        // check that group is no longer listed user 0
        JsonObjectBuilder input = getCommandStubForUser("list-groups", ownUser);
        JsonArray groupsArray
                = runCommand(new ListGroupsCommand(accountManager, mapper),
                input)
                .getJsonArray("groups");
        Assert.assertEquals(0, groupsArray.size());

        // retrieve members array with user 1
        input = getCommandStubForUser("get-group-details", users[1])
                .add("id", groupId);

        JsonArray membersArray
                = runCommand(new GetGroupDetailsCommand(accountManager, mapper),
                input)
                .getJsonObject("group")
                .getJsonArray("members");

        // compare expected set of members (1 and 2) with actual member array
        Set<Integer> expectedMembers = new HashSet<>();
        expectedMembers.add(users[1].id);
        expectedMembers.add(users[2].id);

        Assert.assertEquals(2, membersArray.size());
        Assert.assertTrue(expectedMembers.contains(membersArray.getInt(0)));
        Assert.assertTrue(expectedMembers.contains(membersArray.getInt(1)));

        // check that executing user didn't get an update
        assertNoUpdatesForUser(ownUser);

        // check that the other two users got an update
        for (int i = 1; i <= 2; i++) {
            JsonObject update = getSingleUpdateForUser(users[i]);
            Assert.assertEquals(update.getString("type"),
                    UpdateType.USERGROUP_CHANGE.toString());

            checkTimeCreated(update);
            Assert.assertEquals(update.getInt("creator"), ownUser.id);
            JsonObject updateGroupDesc = update.getJsonObject("usergroup");
            Assert.assertEquals(updateGroupDesc.getString("name"),
                    groupName);

            membersArray = updateGroupDesc.getJsonArray("members");
            Assert.assertEquals(2, membersArray.size());
            Assert.assertTrue(expectedMembers.contains(membersArray.getInt(0)));
            Assert.assertTrue(expectedMembers.contains(membersArray.getInt(1)));
        }
    }

    @Test
    public void invalidGroupId() {
        int invalidID = groupId + 1337;
        JsonObject output = execute(invalidID);
        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Test
    public void leaveTwice() {
        execute(groupId);
        JsonObject output = execute(groupId);
        Assert.assertEquals(output.getInt("error"), 1201);
    }

    private JsonObject execute(int groupID) {

        JsonObjectBuilder input = getCommandStubForUser(this.cmd, ownUser)
                .add("id", groupID);

        return runCommand(new LeaveGroupCommand(accountManager, mapper),
                input);
    }
}