package org.pispeb.treffpunkt.server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treffpunkt.server.abstracttests.GroupDependentTest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class GetGroupDetailsCommandTest extends GroupDependentTest {

    public GetGroupDetailsCommandTest() {
        super("get-group-details");
    }

    @Test
    public void valid() {
        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", groupId);

        JsonObject output = runCommand(getGroupDetailsCommand, inputBuilder);
        Assert.assertTrue(output.containsKey("group"));

        JsonObject group = output.getJsonObject("group");
        Assert.assertEquals(group.getInt("id"), groupId);
        Assert.assertEquals(group.getString("name"), groupName);
        Assert.assertTrue(group.getJsonArray("events").isEmpty());
        Assert.assertTrue(group.getJsonArray("polls").isEmpty());

        JsonArray members = group.getJsonArray("members");
        Assert.assertEquals(members,
                Json.createArrayBuilder()
                        .add(users[0].id)
                        .add(users[1].id)
                        .add(users[2].id)
                        .build());
    }

    @Test
    public void invalidToken() {
        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(accountManager, mapper);
        JsonObjectBuilder inputBuilder = Json.createObjectBuilder()
                .add("cmd","get-group-details")
                .add("token","0")
                .add("id", groupId);

        JsonObject output = runCommand(getGroupDetailsCommand, inputBuilder);
        Assert.assertEquals(output.getInt("error"), 1100);
    }

    @Test
    public void invalidGroupId() {
        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(accountManager, mapper);
        int invalidGroupId = 1337;
        while (invalidGroupId == groupId) {
            invalidGroupId += 23;
        }

        inputBuilder.add("id", invalidGroupId);

        JsonObject output = runCommand(getGroupDetailsCommand, inputBuilder);
        Assert.assertEquals(output.getInt("error"), 1201);
    }
}