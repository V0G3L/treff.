package org.pispeb.treff_server.commands;

import org.junit.Assert;
import org.junit.Test;
import org.pispeb.treff_server.commands.abstracttests.GroupDependentTest;

import javax.json.Json;
import javax.json.JsonObject;

import static org.junit.Assert.*;

public class EditMembershipCommandTest  extends GroupDependentTest {

    public EditMembershipCommandTest() {
        super("edit-membership");
    }

    @Test
    public void valid() {
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(accountManager, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("id", groupId)
                .add("membership", Json.createObjectBuilder()
                        .add("account-id", users[1].id)
                        .add("permissions", permissionInput)
                        .build());

        runCommand(editMembershipCommand, inputBuilder);

        GetMembershipDetailsCommand getMembershipDetailsCommand
                = new GetMembershipDetailsCommand(accountManager, mapper);
        inputBuilder.add("id", users[1].id)
                .add("group-id", groupId);

        JsonObject output =
                runCommand(getMembershipDetailsCommand, inputBuilder);
        JsonObject membershipDesc = output.getJsonObject("membership");
        Assert.assertEquals(users[1].id,
                membershipDesc.getInt("account-id"));
        JsonObject permissions = membershipDesc.getJsonObject("permissions");
        Assert.assertFalse(permissions.getBoolean("edit_any_event"));
        Assert.assertFalse(permissions.getBoolean("create_poll"));
        Assert.assertFalse(permissions.getBoolean("change_permissions"));
        Assert.assertFalse(permissions.getBoolean("manage_members"));
        Assert.assertFalse(permissions.getBoolean("create_event"));
        Assert.assertFalse(permissions.getBoolean("edit_group"));
        Assert.assertFalse(permissions.getBoolean("edit_any_poll"));
    }

    @Test
    public void invalidUserId() {
        int id = 5;
        while (id == ownUser.id || id == users[1].id
                || id == users[2].id || id == users[3].id)
            id += 23;
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(accountManager, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("id", groupId)
                .add("membership", Json.createObjectBuilder()
                        .add("account-id", id)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1200);
    }

    @Test
    public void invalidGroupId() {
        int id = 23;
        while (id == groupId)
            id += 5;
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(accountManager, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("id", id)
                .add("membership", Json.createObjectBuilder()
                        .add("account-id", users[1].id)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1201);
    }

    @Test
    public void notInGroup() {
        EditMembershipCommand editMembershipCommand
                = new EditMembershipCommand(accountManager, mapper);
        JsonObject permissionInput = Json.createObjectBuilder()
                .add("edit_any_event", false)
                .add("create_poll",false)
                .add("change_permissions",false)
                .add("manage_members", false)
                .add("create_event", false)
                .add("edit_group", false)
                .add("edit_any_poll", false)
                .build();
        inputBuilder.add("id", groupId)
                .add("membership", Json.createObjectBuilder()
                        .add("account-id", users[3].id)
                        .add("permissions", permissionInput)
                        .build());

        JsonObject output =
                runCommand(editMembershipCommand, inputBuilder);

        Assert.assertEquals(output.getInt("error"), 1511);
    }

}