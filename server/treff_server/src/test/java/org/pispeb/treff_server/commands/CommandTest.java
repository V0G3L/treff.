package org.pispeb.treff_server.commands;

import org.junit.Before;
import org.pispeb.treff_server.DatabaseDependentTest;

import javax.json.Json;

/**
 * @author tim
 */
public abstract class CommandTest extends DatabaseDependentTest {
    private final String cmd;

    public CommandTest(String cmd) {
        this.cmd = cmd;
    }

    @Before
    public void prepareInput() {
        inputBuilder = Json.createObjectBuilder();
        inputBuilder.add("cmd", cmd);
    }

    protected String buildInput() {
        return inputBuilder.build().toString();
    }

}
