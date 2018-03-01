package org.pispeb.treff_server.commands.abstracttests;

import org.junit.Before;
import org.pispeb.treff_server.DatabaseDependentTest;
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

    private final String cmd;
    private Date startTime;

    public CommandTest(String cmd) {
        this.cmd = cmd;
    }

    @Before
    public void prepareInput() {
        inputBuilder = Json.createObjectBuilder();
        inputBuilder.add("cmd", cmd);
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
     * @param timeCreated the value of the time-created parameter to check
     * @return true if it's valid, false if not
     */
    protected boolean checkTimeCreated(Date timeCreated) {
        //TODO tolerance
        Date currentTime = new Date();
        if (timeCreated.before(startTime) || currentTime.before(timeCreated)) {
            return false;
        }
        return true;
    }

}
