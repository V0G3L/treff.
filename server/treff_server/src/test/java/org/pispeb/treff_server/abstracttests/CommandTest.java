package org.pispeb.treff_server.abstracttests;

import org.junit.Assert;
import org.junit.Before;
import org.pispeb.treff_server.commands.AbstractCommand;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Date;

/**
 * @author tim
 */
public abstract class CommandTest extends DatabaseDependentTest {

    protected JsonObjectBuilder inputBuilder;

    protected final String cmd;
    private Date startTime;

    public CommandTest(String cmd) {
        this.cmd = cmd;
    }

    @Before
    public void prepareInput() {
        inputBuilder = Json.createObjectBuilder();
        inputBuilder.add("cmd", cmd);
    }

    @Before
    public void saveStartTime() {
        startTime = new Date();
    }

    protected String buildInput() {
        return inputBuilder.build().toString();
    }

    protected JsonObject runCommand(AbstractCommand command,
                                    JsonObjectBuilder input) {
        String outputString = command.execute(input.build().toString());
        return toJsonObject(outputString);
    }

    /**
     * states if the time-created parameter od an update is in the valid range
     *
     * @param update the update which time-created shall be checked
     */
    protected void checkTimeCreated(JsonObject update) {
        Date timeCreated
                = new Date(update.getJsonNumber("time-created").longValue());
        Date currentTime = new Date();
        Assert.assertFalse(timeCreated.before(startTime)
                || currentTime.before(timeCreated));
    }

    protected void assertErrorOutput(JsonObject output, int expectedError) {
        Assert.assertEquals(1, output.size());
        Assert.assertEquals(expectedError, output.getInt("error"));
    }

}
