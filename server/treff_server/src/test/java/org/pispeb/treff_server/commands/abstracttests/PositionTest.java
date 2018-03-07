package org.pispeb.treff_server.commands.abstracttests;


import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonObject;
import java.util.Date;

public abstract class PositionTest extends GroupDependentTest {

    protected final long DAY = 86400000;

    public PositionTest(String cmd) {
        super(cmd);
    }

    @Test
    public void invalidGroupID() {
        Assert.assertEquals(1201, execute(users[1], 0,
                new Date().getTime() + DAY).getInt("error"));

        for (int index : new int[]{0, 2}) {
            assertNoUpdatesForUser(users[index]);
        }
    }

    @Test
    public void noMember() {
        Assert.assertEquals(1201, execute(users[3], groupId,
                new Date().getTime() + DAY).getInt("error"));

        for (int index : new int[]{0, 1, 2}) {
            assertNoUpdatesForUser(users[index]);
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

    protected abstract JsonObject execute(User exec, int group, long time);
}
