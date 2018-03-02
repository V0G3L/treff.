package org.pispeb.treff_server.commands;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.GroupDependentTest;
import org.pispeb.treff_server.commands.updates.UpdateType;

import javax.json.*;

import static org.junit.Assert.*;

public class EditGroupCommandTest extends GroupDependentTest {

    public EditGroupCommandTest() {
        super("edit-group");
    }

    @Test
    public void valid() {
        EditGroupCommand editGroupCommand
                = new EditGroupCommand(accountManager, mapper);
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("id",groupId)
                .add("name", "doomedtodie")
                .build();

        inputBuilder.add("group", group);
        runCommand(editGroupCommand, inputBuilder);

        GetGroupDetailsCommand getGroupDetailsCommand
                = new GetGroupDetailsCommand(accountManager, mapper);
        JsonObjectBuilder input =
                getCommandStubForUser("get-group-details", users[0])
                .add("id", groupId);
        JsonObject groupDesc = runCommand(getGroupDetailsCommand, input)
                .getJsonObject("group");
        Assert.assertEquals(groupDesc.getString("name"),
                "doomedtodie");

        Assert.assertEquals(0, getUpdatesForUser(ownUser).size());

        JsonObject update = getSingleUpdateForUser(users[1]);
        Assert.assertEquals(update.getString("type"),
                UpdateType.USERGROUP_CHANGE.toString());

        // TODO check time-created
        Assert.assertEquals(update.getInt("creator"), ownUser.id);
        JsonObject updateGroupDesc = update.getJsonObject("usergroup");
        Assert.assertEquals(updateGroupDesc.getString("name"),
                "groupname");
    }

    @Test
    public void invalidGroupId() {
        int id = 23;
        while (id == groupId)
            id += 42;
        EditGroupCommand editGroupCommand
                = new EditGroupCommand(accountManager, mapper);
        JsonArray members = Json.createArrayBuilder()
                .add(ownUser.id)
                .add(id)
                .build();
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("id",groupId)
                .add("name", "onhisdarkthrone")
                .build();

        inputBuilder.add("group", group);

        JsonObject output = runCommand(editGroupCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Test
    public void noPermissions() {
        // TODO
    }
}