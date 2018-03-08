package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.PositionTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

public class PublishPositionCommandTest extends PositionTest {

    public PublishPositionCommandTest() {
        super("publish-position");
    }

    @Test
    public void validPublication() {
        // get the current membership
        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(accountManager, mapper);
        JsonObjectBuilder input
                = getCommandStubForUser("get-membership-details", ownUser);

        input.add("group-id", groupId).add("id", ownUser.id);
        JsonObject oldMembership
                = runCommand(getMembershipDetailsCommand, input)
                .getJsonObject("membership");

        // execute the command
        long endTime = new Date().getTime() + DAY;
        JsonObject output = execute(ownUser, groupId, endTime);
        Assert.assertTrue(output.isEmpty());

        // check the updates
        for (int index : new int[]{1, 2}) {
            JsonObject update = getSingleUpdateForUser(users[index]);
            Assert.assertEquals(UpdateType.GROUP_MEMBERSHIP_CHANGE.toString(),
                    update.getString("type"));
            Assert.assertEquals(ownUser.id, update.getInt("creator"));
            checkTimeCreated(update);

            //check the membership
            JsonObject newMembership = update.getJsonObject("membership");
            Assert.assertEquals("membership",
                    newMembership.getString("type"));
            Assert.assertEquals(ownUser.id,
                    newMembership.getInt("account-id"));
            Assert.assertEquals(endTime, newMembership
                    .getJsonNumber("sharing-until").longValue());

            //check permissions in membership
            JsonObject oldPermissions
                    = oldMembership.getJsonObject("permissions");
            JsonObject newPermissions
                    = newMembership.getJsonObject("permissions");
            for (String key : new String[]{"edit_any_event", "create_poll",
                    "change_permissions", "manage_members", "create_event",
                    "edit_group", "edit_any_poll"}) {
                Assert.assertEquals(oldPermissions.getBoolean(key),
                        newPermissions.getBoolean(key));
            }
        }
    }

    /**
     * executes the command
     *
     * @param exec  the executing user
     * @param group the id of the group
     * @param time  requested time period to share the position
     * @return the output of the command
     */
    @Override
    protected JsonObject execute(User exec, int group, long time) {
        PublishPositionCommand publishPositionCommand
                = new PublishPositionCommand(accountManager, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, exec);
        input.add("group-id", group).add("time-end", time);
        JsonObject output
                = runCommand(publishPositionCommand, input);

        // Assert that the author and user 3 didn't get an update
        assertNoUpdatesForUser(exec);
        assertNoUpdatesForUser(users[3]);

        return output;
    }
}
