package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.LoginDependentTest;

import javax.json.JsonObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdatePositionCommandTest extends LoginDependentTest {

    public UpdatePositionCommandTest() {
        super("update-position");
    }

    @Test
    public void execute() {
        UpdatePositionCommand updatePositionCommand
                = new UpdatePositionCommand(accountManager, mapper);

        Calendar pastTime = new GregorianCalendar(2002, Calendar.NOVEMBER,
                22, 0, 0, 0);
        inputBuilder.add("latitude", 38.8897)
                .add("longitude", -77.0089)
                .add("time-measured", pastTime.getTime().getTime());

        JsonObject output = runCommand(updatePositionCommand, inputBuilder);

        Assert.assertTrue(output.isEmpty());
    }

    // TODO: check for updates in groups

}