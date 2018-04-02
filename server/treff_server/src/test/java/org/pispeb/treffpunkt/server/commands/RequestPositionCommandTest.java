package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.PositionTest;
import org.pispeb.treffpunkt.server.commands.updates.UpdateType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

public class RequestPositionCommandTest extends PositionTest {

    public RequestPositionCommandTest() {
        super("request-position");
    }

    @Test
    public void validRequest() {
        long endTime = new Date().getTime() + DAY;
        Assert.assertTrue(execute(users[0], groupId,
                endTime).isEmpty());

        for (int index : new int[]{1, 2}) {
            JsonObject update = getSingleUpdateForUser(users[index]);
            Assert.assertEquals(UpdateType.POSITION_REQUEST.toString(),
                    update.getString("type"));
            Assert.assertEquals(users[0].id, update.getInt("creator"));
            checkTimeCreated(update);
            Assert.assertEquals(endTime,
                    update.getJsonNumber("end-time").longValue());
        }
    }

    @Test
    public void invalidTime() {
        Assert.assertEquals(1400, execute(users[2], groupId,
                new Date().getTime() - DAY).getInt("error"));

        for (int index : new int[]{0, 1}) {
            assertNoUpdatesForUser(users[index]);
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
        RequestPositionCommand requestPositionCommand
                = new RequestPositionCommand(sessionFactory, mapper);

        JsonObjectBuilder input
                = getCommandStubForUser(this.cmd, exec);
        input.add("id", group).add("time", time);
        JsonObject output
                = runCommand(requestPositionCommand, input);

        // Assert that the author and user 3 didn't get an update
        assertNoUpdatesForUser(exec);
        assertNoUpdatesForUser(users[3]);

        return output;
    }
}
