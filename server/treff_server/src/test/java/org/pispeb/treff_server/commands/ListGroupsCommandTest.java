package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.abstracttests.GroupDependentTest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ListGroupsCommandTest extends GroupDependentTest {

    public ListGroupsCommandTest() {
        super("list-groups");
    }

    @Test
    public void validOneGroup() {
        ListGroupsCommand listGroupsCommand =
                new ListGroupsCommand(accountManager, mapper);
        JsonObject output = runCommand(listGroupsCommand, inputBuilder);
        JsonArray groups = output.getJsonArray("groups");
        Assert.assertEquals(1, groups.size());
        JsonObject group = groups.getJsonObject(0);
        Assert.assertEquals("usergroup", group.getString("type"));
        Assert.assertEquals(groupId, group.getInt("id"));
        //TODO checksums
    }

    @Test
    public void validTwoGroup() {
        CreateGroupCommand createGroupCommand =
                new CreateGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownUser.id)
                .add(users[2].id)
                .build();
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("name", "oneforthedarklord")
                .add("members", members)
                .build();

        JsonObjectBuilder input =
                getCommandStubForUser("create-group", ownUser)
                        .add("group", group);
        JsonObject createoutput = runCommand(createGroupCommand, input);
        int othergroupid = createoutput.getInt("id");

        ListGroupsCommand listGroupsCommand =
                new ListGroupsCommand(accountManager, mapper);
        JsonObject output = runCommand(listGroupsCommand, inputBuilder);
        JsonArray groups = output.getJsonArray("groups");
        Assert.assertEquals(2, groups.size());

        JsonObject orggroup;
        JsonObject othergroup;
        if (groups.getJsonObject(0).getInt("id") == groupId) {
            orggroup = groups.getJsonObject(0);
            othergroup = groups.getJsonObject(1);
        } else {
            orggroup = groups.getJsonObject(1);
            othergroup = groups.getJsonObject(0);
        }
        for (int i = 0; i < 2; i++) {
        Assert.assertEquals("usergroup",
                groups.getJsonObject(i).getString("type"));
        }

        Assert.assertEquals(groupId, orggroup.getInt("id"));
        //TODO checksum

        Assert.assertEquals(othergroupid, othergroup.getInt("id"));
        //TODO checksum
    }
}