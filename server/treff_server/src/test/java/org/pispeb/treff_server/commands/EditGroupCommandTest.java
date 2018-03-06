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

        assertNoUpdatesForUser(ownUser);

        JsonObject update = getSingleUpdateForUser(users[1]);
        Assert.assertEquals(update.getString("type"),
                UpdateType.USERGROUP_CHANGE.toString());

        // TODO check time-created
        Assert.assertEquals(update.getInt("creator"), ownUser.id);
        JsonObject updateGroupDesc = update.getJsonObject("usergroup");
        Assert.assertEquals("doomedtodie", updateGroupDesc.getString("name"));
    }

    @Test
    public void invalidGroupId() {
        int id = groupId + 1; // invalid ID
        EditGroupCommand editGroupCommand
                = new EditGroupCommand(accountManager, mapper);
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("id", id)
                .add("name", "onhisdarkthrone")
                .build();

        inputBuilder.add("group", group);

        JsonObject output = runCommand(editGroupCommand, inputBuilder);

        assertErrorOutput(output, 1201);
    }

    @Test
    public void noPermissions() {
        EditGroupCommand editGroupCommand
                = new EditGroupCommand(accountManager, mapper);
        JsonObject group = Json.createObjectBuilder()
                .add("type", "usergroup")
                .add("id", groupId)
                .add("name", "onhisdarkthrone")
                .build();

        JsonObjectBuilder input =
                getCommandStubForUser("edit-group", users[2]);
        input.add("group", group);

        JsonObject output = runCommand(editGroupCommand, input);

        assertErrorOutput(output, 2100);
    }
}